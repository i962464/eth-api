package org.pundi.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.pundi.common.ResultCode;
import org.pundi.constant.NetworkEnum;
import org.pundi.dto.EthTransferDTO;
import org.pundi.dto.MultipleTransferQueue;
import org.pundi.dto.MultipleTransferQueue.TransferInfo;
import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.entity.EthScanBlockEntity;
import org.pundi.entity.UserAddressEntity;
import org.pundi.entity.UserEntity;
import org.pundi.service.CurrencyInfoService;
import org.pundi.service.DepositTxRecordService;
import org.pundi.service.EthScanBlockService;
import org.pundi.service.EtherScanRecordService;
import org.pundi.service.TransferService;
import org.pundi.service.UserAddressService;
import org.pundi.service.UserService;
import org.pundi.service.UserTransactionsService;
import org.pundi.util.EthereumUtil;
import org.pundi.vo.EtherScanVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 18:48:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

  @Value("${pundi.eth.tx-scan.default-block}")
  private BigInteger defaultScanBlock;

  private final CurrencyInfoService currencyInfoService;
  private final UserService userService;
  private final UserAddressService addressService;
  private final UserTransactionsService transactionsService;
  private final EthScanBlockService ethScanBlockService;
  private final EtherScanRecordService etherScanRecordService;
  private final DepositTxRecordService depositTxRecordService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String ethTransfer(EthTransferDTO dto) {

    Integer uid = dto.getUid();
    String tokenAddress = dto.getTokenAddress();
    BigInteger amount = dto.getAmount();
    String toAddress = dto.getToAddress();

    //用户校验
    UserEntity user = userService.getById(uid);
    if (Objects.isNull(user)) {
      throw new RuntimeException(ResultCode.USER_NOT_FOUND.getMsg());
    }

    //地址格式校验
    boolean validAddress = EthereumUtil.isValidAddress(toAddress);
    if (!validAddress) {
      throw new RuntimeException(ResultCode.ADDRESS_VALID_ERROR.getMsg());
    }

    //token校验
    String ASSET = "ETH";
    CurrencyInfoEntity currencyInfo;
    if (StringUtils.isNotBlank(tokenAddress)) {
      currencyInfo = currencyInfoService.queryByNetworkIdAndAsset(NetworkEnum.ETH.getCode(), ASSET);
    } else {
      currencyInfo = currencyInfoService.queryByNetworkIdAndContract(NetworkEnum.ETH.getCode(), tokenAddress);
    }
    if (Objects.isNull(currencyInfo)) {
      throw new RuntimeException(ResultCode.CURRENCY_NOT_FOUND.getMsg());
    }

    //用户地址校验
    UserAddressEntity addressEntity = addressService.findByNetWorkAssetUid(NetworkEnum.ETH.getCode(), ASSET, uid);
    if (Objects.isNull(addressEntity)) {
      throw new RuntimeException(ResultCode.ADDRESS_NOT_EXIST.getMsg());

    }
    //交易
    try {
      String txHash;
      if (StringUtils.isNotBlank(tokenAddress)) {
        txHash = EthereumUtil.tokenTransfer(addressEntity.getAddress(), toAddress, tokenAddress, amount, addressEntity.getPrivateKey());
      } else {
        txHash = EthereumUtil.ethTransfer(addressEntity.getAddress(), toAddress, amount, addressEntity.getPrivateKey());
      }
      //保存记录
      transactionsService.saveRecord(uid, addressEntity.getAddress(), toAddress, amount, currencyInfo, txHash);
      return txHash;
    } catch (ExecutionException | InterruptedException | IOException e) {
      throw new RuntimeException(ResultCode.ERROR.getMsg());
    }
  }

  @Override
  public void scanUserTxs() throws IOException {

    //上次同步区块
    BigInteger lastSyncBlock;
    EthScanBlockEntity ethScanBlock = ethScanBlockService.getLastBlock();
    if (Objects.nonNull(ethScanBlock) && Objects.nonNull(ethScanBlock.getEndBlock())) {
      lastSyncBlock = ethScanBlock.getEndBlock();
    } else {
      lastSyncBlock = defaultScanBlock;
    }
    //最新区块
    BigInteger newBlock = EthereumUtil.getLastBlock();

    //用户地址
    List<UserAddressEntity> addressEntityList = addressService.list();
    List<String> addressList = addressEntityList.stream().map(e -> e.getAddress().toLowerCase()).collect(Collectors.toList());
    Map<String, Integer> addressUidMap = addressEntityList.stream()
        .collect(Collectors.toMap(UserAddressEntity::getAddress, UserAddressEntity::getUid));

    // 遍历开始区块和结束区块之间的每个区块
    for (BigInteger blockNumber = lastSyncBlock; blockNumber.compareTo(newBlock) <= 0; blockNumber = blockNumber.add(BigInteger.ONE)) {

      List<EtherScanVO> etherScanVOS = Lists.newArrayList();

      // 获取当前区块的详细信息,TransactionResult 对象表示一个交易结果，而 TransactionReceipt 对象则表示一个交易收据
      //EthBlock ethBlock = EthereumUtil.getWeb3j().ethGetBlockByNumber().send();
      //List<TransactionResult> transactions = ethBlock.getBlock().getTransactions();
      // transactionResult 区分ETH，ERC20 token 只能根据ETH 的amount 是否大于0来区分，但是to地址有可能是合约地址，不是真正的用户地址
      List<TransactionReceipt> transactionReceipts = EthereumUtil.getWeb3j().ethGetBlockReceipts(DefaultBlockParameter.valueOf(blockNumber)).send()
          .getBlockReceipts().get();
      for (TransactionReceipt tx : transactionReceipts) {
        if (addressList.contains(tx.getFrom()) || addressList.contains(tx.getTo())) {
          List<Log> logs = tx.getLogs();
          if (CollectionUtils.isNotEmpty(logs)) {
            //Erc20 token
            etherScanVOS.addAll(EthereumUtil.buildErc20Record(logs));
          } else {
            //ETH
            etherScanVOS.addAll(EthereumUtil.buildETHRecord(tx));
          }
        }
      }
      //保存记录
      if (CollectionUtils.isNotEmpty(etherScanVOS)) {
        etherScanRecordService.saveRecord(etherScanVOS);

        //新增一个需求， 添加充值交易归集。deposit_tx_record,以后汇集查询这张表不影响其他业务
        List<EtherScanVO> depositScans = etherScanVOS.stream().filter(e -> addressList.contains(e.getReceiverAddress())).collect(Collectors.toList());
        List<EtherScanVO> newDepositScans = depositScans.stream().peek(e -> e.setUid(addressUidMap.get(e.getReceiverAddress())))
            .collect(Collectors.toList());
        depositTxRecordService.saveRecord(newDepositScans);
        //todo 这里还可以创建充币的订单
      }
      //更新区块号
      ethScanBlockService.updateBlockNumber(blockNumber);
    }
  }

  @Override
  public IPage<EtherScanVO> getPageTransactions(String symbol, String address, BigInteger startBlock, BigInteger endBlock, Integer page,
                                                Integer pageSize) {

    CurrencyInfoEntity currencyInfo = currencyInfoService.queryByNetworkIdAndAsset(60, symbol);
    if (Objects.isNull(currencyInfo)) {
      throw new RuntimeException(ResultCode.CURRENCY_NOT_FOUND.getMsg());
    }
    String contractAddress = currencyInfo.getContractAddress();
    return etherScanRecordService.pageByParams(address, contractAddress, startBlock, endBlock, page, pageSize);
  }

  @Override
  public void multipleTransfer() throws IOException, ExecutionException, InterruptedException {

    MultipleTransferQueue multipleQueue = new MultipleTransferQueue();
    multipleQueue.addToQueue(TransferInfo.builder().toAddress("0x76f043cB90FaAFf31CC28091319f3F200df93f71").amount(BigDecimal.ONE).build());
    multipleQueue.addToQueue(TransferInfo.builder().toAddress("0x76f043cB90FaAFf31CC28091319f3F200df93f72").amount(BigDecimal.ONE).build());
    multipleQueue.addToQueue(TransferInfo.builder().toAddress("0x76f043cB90FaAFf31CC28091319f3F200df93f73").amount(BigDecimal.ONE).build());
    multipleQueue.addToQueue(TransferInfo.builder().toAddress("0x76f043cB90FaAFf31CC28091319f3F200df93f74").amount(BigDecimal.ONE).build());
    multipleQueue.addToQueue(TransferInfo.builder().toAddress("0x76f043cB90FaAFf31CC28091319f3F200df93f75").amount(BigDecimal.ONE).build());

    String tokenAddress = "0x67550Df3290415611F6C140c81Cd770Ff1742cb9";
    String fromAddress = "0x8ce4092e890c5e21d1596156edc73ab00242b20d";
    String fromPrivateKey = "11e807ffd2af91ad19a093c9613f116139848b6bf10f9fb8f2f0f138f7b44ec4";

    while (!multipleQueue.getTransactionQueue().isEmpty()) {
      //队列中获取并移除下一个交易信息（TransferInfo）。这样可以确保每次处理队列时，都是处理队头的交易信息，从而按顺序发送交易
      TransferInfo transferInfo = multipleQueue.getTransactionQueue().poll();
      String toAddress = transferInfo.getToAddress();
      BigInteger amount = Convert.toWei(transferInfo.getAmount(), Unit.ETHER).toBigInteger();
      String txHash = EthereumUtil.tokenTransfer(fromAddress, toAddress, tokenAddress, amount, fromPrivateKey);
      log.info(">>>multipleTransfer txHash = {}", txHash);
    }

  }


}
