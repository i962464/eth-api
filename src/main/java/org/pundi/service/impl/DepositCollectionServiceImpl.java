package org.pundi.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.pundi.constant.AssetCollectionStatusEnum;
import org.pundi.constant.NetworkEnum;
import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.entity.DepositTxRecordEntity;
import org.pundi.entity.UserAddressEntity;
import org.pundi.service.CurrencyInfoService;
import org.pundi.service.DepositCollectionService;
import org.pundi.service.DepositTxRecordService;
import org.pundi.service.UserAddressService;
import org.pundi.util.EthereumUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.web3j.crypto.RawTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月16日 09:53:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepositCollectionServiceImpl implements DepositCollectionService {

  private final CurrencyInfoService currencyInfoService;
  private final DepositTxRecordService depositTxRecordService;
  private final RedisTemplate redisTemplate;

  private static final String DEPOSIT_COLLECTING_FILE_MAP = "deposit_collecting_file_map:";

  /**
   * 以下是伪代码
   * @throws IOException
   */
  @Override
  public void depositCollection() throws IOException {

    List<CurrencyInfoEntity> currencyInfoList = currencyInfoService.list();

    //获取当前区块高度， 暂时先解析一个区块，方便测试
    BigInteger lastBlock = EthereumUtil.getLastBlock();

    //支持的网络
    List<Integer> networkList = currencyInfoList.stream().map(CurrencyInfoEntity::getNetworkId).distinct().collect(Collectors.toList());

    //获取充值记录
    List<DepositTxRecordEntity> depositTxRecordEntityList = depositTxRecordService.getUnCollections(null);
    //uid->list
    Map<Integer, List<DepositTxRecordEntity>> depositRecordMap = depositTxRecordEntityList.stream()
        .collect(Collectors.groupingBy(DepositTxRecordEntity::getUid));

    //按用户归集
    depositRecordMap.forEach((uid, records) -> {

      //遍历网络列表
      for (Integer networkId : networkList) {

        //目前只处理ETH网络的token, 后期可根据策略模式拆分
        if (!networkId.equals(NetworkEnum.ETH.getCode())) {
          continue;
        }

        //按token分组
        Map<String, List<DepositTxRecordEntity>> recordList = records.stream().collect(Collectors.groupingBy(DepositTxRecordEntity::getTokenAddress));

        //获取token精度
        Map<String, Integer> currencyDecimalsMap = currencyInfoList.stream()
            .collect(Collectors.toMap(CurrencyInfoEntity::getContractAddress, CurrencyInfoEntity::getDecimals));

        //token->list
        for (Entry<String, List<DepositTxRecordEntity>> txRecord : recordList.entrySet()) {
          String tokenAddress = txRecord.getKey();

          List<DepositTxRecordEntity> newTxRecord = txRecord.getValue().stream()
              .filter(e -> lastBlock.subtract(e.getBlockNum()).compareTo(BigInteger.ZERO) > 0).collect(Collectors.toList());

          //判断当前地址eth是否够本次交易
          BigInteger gasPrice = null;
          try {
            gasPrice = EthereumUtil.getGasPrice();
          } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
          }
          BigDecimal gasPriceBD = Convert.fromWei(gasPrice.toString(), Unit.GWEI).setScale(0, RoundingMode.DOWN);


          if (StringUtils.isBlank(tokenAddress)) {
            //同erc20Token构建、签名、广播、保存到redis

          } else {
            // 先归集代币，等下一次任务找机会归集主网币
            //累计当前token充值金额
            BigDecimal txAmount = newTxRecord.stream()
                .map(DepositTxRecordEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (txAmount.compareTo(BigDecimal.ONE) < 0) {
              //这里目前先随便加了个限制， 实际情况需要读取每个token的扩展信息， 看它的归集配置
              log.debug("doMerge |  网络:{} ,币种{} 金额:{}  归集阈值: {} 小于归集阈值，暂不归集", networkId, tokenAddress, txAmount, BigDecimal.ONE);
              return;
            }

            try {
              //  1-调用API服务创建离线交易（1，2，3 需要提到另外的服务），注意组装nonce的时候， 相同的地址需要累加
              String fromAddress = txRecord.getValue().get(0).getReceiverAddress();
              RawTransaction rawTransaction = EthereumUtil.buildTokenRawTx(fromAddress, "", tokenAddress,
                  Convert.toWei(txAmount, Unit.ETHER).toBigInteger());
              //  2-对交易进行签名
              byte[] signedTransaction = EthereumUtil.signTransaction(rawTransaction, "查询用户地址私钥，也可以从HD推导");
              //  3-导出签名后的交易数据为十六进制字符串
              String hexValue = Numeric.toHexString(signedTransaction);

              // 4-广播交易
              String txHash = EthereumUtil.sendSignedTransaction(hexValue);

              // 5-保存到redis
              LocalDateTime now = LocalDateTime.now();
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
              String filename = tokenAddress + "_" + now.format(formatter) + ".json";
              redisTemplate.opsForHash().put(DEPOSIT_COLLECTING_FILE_MAP, filename, txHash);

              // 5-更新状态为归集中，并更新归集hash
              depositTxRecordService.updateStatusAndHash(newTxRecord, hexValue, AssetCollectionStatusEnum.COLLECTING);
              log.info(">>>>>>归集完成， UID={}, address={}, txHash={}", uid, fromAddress, txHash);
            } catch (ExecutionException | InterruptedException | IOException e) {
              //构建交易失败
              log.error("构建交易失败...");
            }


          }


        }

      }

    });


  }

}
