package org.pundi.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.params.MainNetParams;
import org.pundi.common.ResultCode;
import org.pundi.exception.BusinessRuntimeException;
import org.pundi.vo.EtherScanVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 10:38:00
 */
@Slf4j
@Component
public class EthereumUtil {

  public static String TRANSFER_TOPIC_NAME = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
  public static String ETHER_ZERO_ADDRESS = "0x0000000000000000000000000000000000000000000000000000000000000000";

  /**
   * rpc 节点
   */
  @Value("${api.infura.url}")
  private String web3RpcEndpoint;

  @PostConstruct
  public void init() {
    EthereumUtil.WEB3_RPC_ENDPOINT = web3RpcEndpoint;
  }

  /**
   * set注入
   */
  private static String WEB3_RPC_ENDPOINT;

  public static void setWeb3RpcEndpoint(String endpoint) {
    EthereumUtil.WEB3_RPC_ENDPOINT = endpoint;
  }

  /**
   * 获取web3j 对象
   */
  private static volatile Web3j _web3j;

  public static Web3j getWeb3j() {
    if (_web3j == null) {
      synchronized (EthereumUtil.class) {
        _web3j = Web3j.build(new HttpService(WEB3_RPC_ENDPOINT));
      }
    }
    return _web3j;
  }


  /**
   * 钱包地址校验
   * @param address
   * @return
   */
  public static boolean isValidAddress(final String address) {
    String checksumAddress;
    try {
      checksumAddress = Keys.toChecksumAddress(address);
    } catch (Exception e) {
      log.error("isValidAddress | address:{}, err:", address, e);
      return false;
    }
    return Numeric.prependHexPrefix(checksumAddress).equals(checkedAddress(checksumAddress));
  }

  public static boolean isCheckedAddress(final String address) {

    try {
      String cleanAddress = address.trim();
      String checksumAddress = Keys.toChecksumAddress(cleanAddress);
      String lowerCase = checksumAddress.toLowerCase();
      String upperCase = checksumAddress.toUpperCase();
      if (StringUtils.equalsAny(cleanAddress, lowerCase, upperCase)) {
        return Numeric.prependHexPrefix(checksumAddress).equals(checkedAddress(checksumAddress));
      } else {
        return StringUtils.equals(checksumAddress, cleanAddress);
      }
    } catch (Exception e) {
      log.error("isCheckedAddress | address:{}, err:", address, e);
      return false;
    }
  }

  public static String checkedAddress(final String address) {

    final String cleanAddress = Numeric.cleanHexPrefix(address).toLowerCase();
    //
    StringBuilder o = new StringBuilder();
    String keccak = Hash.sha3String(cleanAddress);
    char[] checkChars = keccak.substring(2).toCharArray();

    char[] cs = cleanAddress.toLowerCase().toCharArray();
    for (int i = 0; i < cs.length; i++) {
      char c = cs[i];
      c = (Character.digit(checkChars[i], 16) & 0xFF) > 7 ? Character.toUpperCase(c) : Character.toLowerCase(c);
      o.append(c);
    }
    return Numeric.prependHexPrefix(o.toString());
  }

  /**
   * web3j 创建地址
   * @param pwd
   */
  public static Pair<String, String> createAccount(String pwd) {
    try {
      if (StringUtils.isBlank(pwd)) {
        pwd = UUID.randomUUID().toString();
      }
      ECKeyPair ecKeyPair = Keys.createEcKeyPair();
      BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
      String privateKey = privateKeyInDec.toString(16);
      WalletFile aWallet = Wallet.createLight(pwd, ecKeyPair);
      String address = aWallet.getAddress();
      if (address.startsWith("0x")) {
        address = address.substring(2).toLowerCase();
      } else {
        address = address.toLowerCase();
      }
      address = "0x" + address;
      System.out.println("pwd：" + pwd);
      System.out.println("地址：" + address);
      System.out.println("秘钥：" + privateKey);
      return Pair.of(address, privateKey);
    } catch (InvalidAlgorithmParameterException |
        CipherException | NoSuchProviderException | NoSuchAlgorithmException e) {
      throw new BusinessRuntimeException(ResultCode.SYS_ERROR);
    }
  }

  /**
   * BIP44 推导 Ethereum 地址
   *
   * @param extKey       扩展公钥
   * @param addressIndex 推导路径
   * @return 地址
   */
  public static String deriveAddressFromExtendPubKey(String extKey, int addressIndex) {

    DeterministicKey parentKey = DeterministicKey.deserializeB58(extKey, MainNetParams.get());
    DeterministicKey childKey = HDKeyDerivation.deriveChildKey(parentKey, addressIndex);
    ECKey uncompressedChildKey = childKey.decompress();
    // 以太坊需要把前缀去掉（0x04前缀表示未压缩）
    String hexKey = uncompressedChildKey.getPublicKeyAsHex().substring(2);
    String addr = Keys.getAddress(hexKey);

    return Keys.toChecksumAddress(addr);
  }

  //todo 这个方法有问题，后续修改
  public static String getHDPrivateKey(String extKey, int addressIndex) {

    DeterministicKey parentKey = DeterministicKey.deserializeB58(extKey, MainNetParams.get());
    DeterministicKey childKey = HDKeyDerivation.deriveChildKey(parentKey, addressIndex);
    String privateKey = childKey.getPrivateKeyAsHex();
    return privateKey;
  }

  // 将字节数组转换为十六进制字符串
  private static String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  /**
   * 查询获取账户的 Ether 余额信息
   *
   * @param address 目标地址
   * @return 账户信息
   * @throws IllegalArgumentException 地址校验错误
   * @throws IOException              网络异常
   */
  public static BigInteger getBalance(String address) throws IllegalArgumentException, IOException {

    if (!WalletUtils.isValidAddress(address)) {
      log.warn("Ethereum 地址错误");
      throw new IllegalArgumentException("Ethereum 地址错误");
    }
    String addr = Keys.toChecksumAddress(address);
    Web3j web3j = getWeb3j();
    return web3j.ethGetBalance(addr, DefaultBlockParameterName.LATEST).send().getBalance();
  }

  /**
   * 获取指定地址的 ERC-20 Token  余额
   *
   * @param address         查询地址
   * @param contractAddress 合约地址
   * @return Token 余额
   * @throws ExecutionException   网络执行异常
   * @throws InterruptedException 网络中断异常
   */
  public static BigInteger getBalance(String address, String contractAddress) throws ExecutionException,
      InterruptedException {

    Web3j web3j = getWeb3j();
    Function function = new Function("balanceOf",
        Collections.singletonList(new Address(address)),
        Collections.singletonList(new TypeReference<Address>() {
        }));
    String encode = FunctionEncoder.encode(function);
    Transaction ethCallTransaction = Transaction.createEthCallTransaction(address, contractAddress, encode);
    EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
    String result = ethCall.getResult().substring(2);
    // 兼容测试网在 没有 TOKEN 情况下直接 返回 "0x" 导致异常的情况
    return new BigInteger(result.length() > 0 ? result : "0", 16);
  }

  /**
   * 发送一笔 ETH 交易
   *
   * @param from   付款地址
   * @param to     首款地址
   * @param value  金额
   * @param prvKey 付款地址私钥
   * @return 交易 hash
   * @throws ExecutionException   执行异常
   * @throws InterruptedException 中断异常
   */
  public static String ethTransfer(String from, String to, BigInteger value, String prvKey)
      throws ExecutionException, InterruptedException, IOException {

    // 创建RawTransaction交易对象
    RawTransaction rawTransaction = buildEthRawTx(from, to, value);
    // 构建私钥
    Credentials credentials = Credentials.create(prvKey);
    // 签名Transaction
    byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    String hexValue = Numeric.toHexString(signMessage);
    // 发送交易
    EthSendTransaction ethSendTransaction = getWeb3j().ethSendRawTransaction(hexValue).sendAsync().get();

    return ethSendTransaction.getTransactionHash();
  }

  /**
   * ERC20 Token 转账
   *
   * @param from            转出地址
   * @param to              转入地址
   * @param contractAddress token 合约地址
   * @param value           转出金额
   * @param prvKey          转出地址的私钥
   * @return 交易 hash
   * @throws ExecutionException   执行异常
   * @throws InterruptedException 中断异常
   */
  public static String tokenTransfer(String from, String to, String contractAddress, BigInteger value, String prvKey)
      throws ExecutionException, InterruptedException, IOException {

    // 构建建议
    RawTransaction rawTx = buildTokenRawTx(from, to, contractAddress, value);
    // 构建私钥
    Credentials credentials = Credentials.create(prvKey);
    // 签名Transaction
    byte[] signMessage = TransactionEncoder.signMessage(rawTx, credentials);
    String hexValue = Numeric.toHexString(signMessage);
    // 发送交易
    EthSendTransaction ethSendTransaction = getWeb3j().ethSendRawTransaction(hexValue).sendAsync().get();

    return ethSendTransaction.getTransactionHash();
  }

  /**
   * Token 转账，原始交易封装， gasPrice 和 gasLimit 采用默认值
   *
   * @param from            转出地址
   * @param to              转入地址
   * @param contractAddress token 合约地址
   * @param amount          转出金额
   * @return 未签名交易
   * @throws ExecutionException   网络执行异常
   * @throws InterruptedException 网络中断异常
   */
  public static RawTransaction buildTokenRawTx(String from, String to, String contractAddress, BigInteger amount)
      throws ExecutionException, InterruptedException, IOException {

    // 获取 GasPrice
    BigInteger gasPrice = getGasPrice();
    // 计算 10% 的增量
    BigDecimal increment = new BigDecimal("0.10");
    gasPrice = new BigDecimal(gasPrice).multiply(increment.add(BigDecimal.ONE)).toBigInteger(); // 1 * (1 + 10%)
    return buildTokenRawTx(from, to, contractAddress, amount, gasPrice);
  }

  public static RawTransaction buildTokenRawTx(
      String from,
      String to,
      String contractAddress,
      BigInteger amount,
      BigInteger gasPrice) throws ExecutionException, InterruptedException, IOException {
    // 获取nonce，交易笔数
    BigInteger nonce = getNonce(from);
    // 构建输入参数
    List<Type> inputArgs = new ArrayList<>();
    inputArgs.add(new Address(to));
    inputArgs.add(new Uint256(amount));
    // 合约返回值容器
    List<TypeReference<?>> outputArgs = new ArrayList<>();
    // 封装转账交易
    Function funcABI = new Function("transfer", inputArgs, outputArgs);
    String data = FunctionEncoder.encode(funcABI);
    // 构建交易
//    Transaction transaction = Transaction.createFunctionCallTransaction(from, nonce, gasPrice, null,
//        contractAddress, data);
    // 计算gasLimit
//    BigInteger gasLimit = getTxGasLimit(transaction).multiply(BigInteger.valueOf(1L));
    BigInteger gasLimit = getGaslimit("transfer", from, contractAddress);

    // 查询调用者余额，检测余额是否充足
    BigInteger balance = getBalance(from);
    // balance <  gasLimit ??
    if (balance.compareTo(gasLimit.multiply(gasPrice)) < 0) {
      BigDecimal gas = Convert.fromWei(new BigDecimal(gasLimit.multiply(gasPrice)), Unit.ETHER);
      log.warn("Ethereum Token 转账，转出地址 ETH 余额不足, 预估手续费为：{}", gas.toString());
      throw new RuntimeException("insufficient funds");
    }
    return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
  }

  /**
   * 交易手续费由gasPrice 和gasLimit来决定，实际花费的交易手续费是gasUsed * gasPrice。
   * 所有这两个值可以自定义，也可以使用系统参数获取当前两个值
   *
   * @param from  转出地址
   * @param to    转入地址
   * @param value 金额数量
   * @return 未签名交易
   */
  public static RawTransaction buildEthRawTx(String from, String to, BigInteger value)
      throws ExecutionException, InterruptedException, IOException {

    // 获取 GasPrice
    BigInteger gasPrice = getGasPrice();

    // 计算 10% 的增量
    BigDecimal increment = new BigDecimal("0.10");
    gasPrice = new BigDecimal(gasPrice).multiply(increment.add(BigDecimal.ONE)).toBigInteger(); // 1 * (1 + 10%)

    return buildEthRawTx(from, to, value, gasPrice, null);
  }


  /**
   * 获取当前以太坊网络中最近一笔交易的 gasPrice
   *
   * @return gasPrice
   * @throws ExecutionException   执行异常
   * @throws InterruptedException 网络异常
   */
  public static BigInteger getGasPrice() throws ExecutionException, InterruptedException {

    Web3j web3j = getWeb3j();
    EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();

    return ethGasPrice.getGasPrice();
  }

  public static RawTransaction buildEthRawTx(
      String from
      , String to
      , BigInteger value
      , BigInteger gasPrice
      , BigInteger gasLimit) throws ExecutionException, InterruptedException, IOException {
    // 获取nonce，交易笔数
    BigInteger nonce = getNonce(from);
    // 构建交易
    if (Objects.isNull(gasLimit)) {
      Transaction transaction = Transaction.createEtherTransaction(from, nonce, gasPrice, null, to, value);
      gasLimit = getTxGasLimit(transaction);
    }
    // 查询调用者余额，检测余额是否充足
    BigInteger balance = getBalance(from);
    if (balance.compareTo(value.add(gasLimit.multiply(gasPrice))) < 0) {
      log.warn("Eth 转账，转出地址余额不足 balance={} needFee={}", balance, gasLimit.multiply(gasPrice));
      throw new RuntimeException("insufficient funds");
    }
    // 创建RawTransaction交易对象
    return RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);
  }

  /**
   * 获取nonce，交易笔数
   *
   * @param from 交易地址
   * @return 交易笔数
   * @throws ExecutionException   执行异常
   * @throws InterruptedException 网络异常
   */
  public static BigInteger getNonce(String from) throws ExecutionException, InterruptedException {

    Web3j web3j = getWeb3j();
    EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from,
        DefaultBlockParameterName.LATEST).sendAsync().get();

    return transactionCount.getTransactionCount();
  }

  /**
   * 获取最新区块
   * @return
   * @throws IOException
   */
  public static BigInteger getLastBlock() throws IOException {
    Web3j web3j = getWeb3j();
    return web3j.ethBlockNumber().send().getBlockNumber();
  }

  /**
   * 估算手续费上限
   *
   * @param transaction 交易对象
   * @return gas 上限
   */
  public static BigInteger getTxGasLimit(Transaction transaction) {

    Web3j web3j = getWeb3j();
    try {
      EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
      if (ethEstimateGas.hasError()) {
        throw new RuntimeException(ethEstimateGas.getError().getMessage());
      }
      return ethEstimateGas.getAmountUsed();
    } catch (IOException e) {
      throw new RuntimeException("net error");
    }
  }


  /**
   * @param cname 调用合约的名字
   * @param address 调用合约的地址
   * @param contractAddress 合约地址
   * @return
   * @throws IOException
   */
  public static BigInteger getGaslimit(String cname, String address, String contractAddress) throws IOException {
    Web3j web3j = getWeb3j();
    String function = cname;
    List<Type> inputParams = List.of();
    Function contractFunction = new Function(function, inputParams, Collections.emptyList());

    EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(
        Transaction.createEthCallTransaction(
            address,
            contractAddress,
            FunctionEncoder.encode(contractFunction)
        )
    ).send();

    if (ethEstimateGas.hasError()) {
//            System.out.println("获取 gas 上限时发生错误: " + ethEstimateGas.getError().getMessage());
      return BigInteger.valueOf(150_000);
    } else {
      return ethEstimateGas.getAmountUsed();
    }
  }

  /**
   * 解析Erc20 token交易信息
   * @param logs
   * @return
   */
  public static List<EtherScanVO> buildErc20Record(List<Log> logs) {
    List<EtherScanVO> etherScanVOS = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(logs)) {
      for (Log l : logs) {
        List<String> topics = l.getTopics();

        //解析topic 中transfer交易
        if (topics.get(0).equalsIgnoreCase(TRANSFER_TOPIC_NAME)
            && !topics.get(1).equalsIgnoreCase(ETHER_ZERO_ADDRESS)
            && !topics.get(2).equalsIgnoreCase(ETHER_ZERO_ADDRESS)) {

          byte[] decodedData = Numeric.hexStringToByteArray(l.getData());
          BigInteger amount = Numeric.toBigInt(decodedData);
          String from = Numeric.toHexStringWithPrefix(Numeric.toBigInt(topics.get(1)));
          String to = Numeric.toHexStringWithPrefix(Numeric.toBigInt(topics.get(2)));
          log.info(">>>from={}, to={}, amount={}", from, to, amount);

          String tokenAddress = l.getAddress();
          String blockHash = l.getBlockHash();
          BigInteger blockNumber = l.getBlockNumber();
          String transactionHash = l.getTransactionHash();

          etherScanVOS.add(EtherScanVO.builder()
              .blockNum(blockNumber)
              .blockHash(blockHash)
              .senderAddress(from)
              .receiverAddress(to)
              .amount(amount)
              .tokenName(getTokenSymbol(tokenAddress))
              .tokenAddress(tokenAddress)
              .transactionHash(transactionHash)
              .build());
        }
      }
    }
    return etherScanVOS;
  }

  /**
   * 解析ETH 交易信息
   * 需要获取transaction结果的amount
   * @param tx
   * @return
   */
  public static List<EtherScanVO> buildETHRecord(TransactionReceipt tx) {
    Web3j web3j = getWeb3j();
    EthTransaction ethTransaction;
    try {
      ethTransaction = web3j.ethGetTransactionByHash(tx.getTransactionHash()).send();
      BigInteger amount = ethTransaction.getTransaction().get().getValue();
      return List.of(
          EtherScanVO.builder()
              .blockNum(tx.getBlockNumber())
              .blockHash(tx.getBlockHash())
              .senderAddress(tx.getFrom())
              .receiverAddress(tx.getTo())
              .amount(amount)
              .tokenName("ETH")
              .transactionHash(tx.getTransactionHash())
              .build());
    } catch (IOException e) {
      throw new RuntimeException("net error");
    }
  }


  public static String getCoinName(String contractAddress) throws Exception {

    Web3j web3j = getWeb3j();
    // 创建获取代币名称的方法
    Function function = new Function(
        "name",  // 要调用的方法名称
        Collections.emptyList(),  // 方法参数
        Collections.singletonList(new TypeReference<Utf8String>() {
        }));  // 返回值类型

    // 编码方法调用
    String encodedFunction = FunctionEncoder.encode(function);

    // 调用智能合约方法
    EthCall ethCall = web3j.ethCall(Transaction.createEthCallTransaction(
                "0x0000000000000000000000000000000000000000",  // 从哪个地址调用，这里填写一个空地址
                contractAddress,  // 要调用的合约地址
                encodedFunction),  // 调用函数的数据
            DefaultBlockParameterName.LATEST)  // 使用最新的区块
        .send();

    // 解码返回值
    List<Type> decode = FunctionReturnDecoder.decode(
        ethCall.getValue(), function.getOutputParameters());
    return ((Utf8String) decode.get(0)).getValue();
  }

  public static String getTokenSymbol(String contractAddress) {
    Web3j web3j = getWeb3j();
    // 创建获取代币名称的方法
    Function function = new Function(
        "symbol",
        Collections.emptyList(),
        Collections.singletonList(new TypeReference<Utf8String>() {
        }));

    // 编码方法调用
    String encodedFunction = FunctionEncoder.encode(function);
    // 调用智能合约方法
    EthCall ethCall = null;
    try {
      ethCall = web3j.ethCall(Transaction.createEthCallTransaction(
                  "0x0000000000000000000000000000000000000000",  // 从哪个地址调用，这里填写一个空地址
                  contractAddress,  // 要调用的合约地址
                  encodedFunction),  // 调用函数的数据
              DefaultBlockParameterName.LATEST)  // 使用最新的区块
          .send();
    } catch (IOException e) {
      throw new RuntimeException("net error");
    }

    // 解码返回值
    List<Type> decode = FunctionReturnDecoder.decode(
        ethCall.getValue(), function.getOutputParameters());
    return ((Utf8String) decode.get(0)).getValue();
  }


  /**
   * 将 BigInteger 转化为 BigDecimal, 默认采用 "银行家舍入"
   *
   * @param val      原始值
   * @param decimals 精度
   * @return 结果
   */
  public static BigDecimal withDecimal(BigInteger val, int decimals) {

    return withDecimal(val, decimals, RoundingMode.HALF_EVEN);
  }

  /**
   * 将 BigInteger 转化为 BigDecimal
   *
   * @param val      原始值
   * @param decimals 精度
   * @param rm       舍入方式
   * @return 结果
   */
  public static BigDecimal withDecimal(BigInteger val, int decimals, RoundingMode rm) {

    BigDecimal value = new BigDecimal(val);
    BigDecimal precision = BigDecimal.valueOf(10L).pow(decimals);

    return value.divide(precision, decimals, rm);
  }

  /**
   * 对交易进行签名
   * @param rawTransaction 未签名的交易
   * @param privateKey 发送方私钥
   * @return 签名后的交易数据
   */
  public static byte[] signTransaction(RawTransaction rawTransaction, String privateKey) {
    Credentials credentials = Credentials.create(privateKey);
    return TransactionEncoder.signMessage(rawTransaction, credentials);
  }

  /**
   * 发送已签名的交易到以太坊网络
   * @param signedTransaction 签名后的交易数据
   * @return 交易哈希
   * @throws IOException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static String sendSignedTransaction(String signedTransaction) throws IOException, InterruptedException, ExecutionException {
    EthSendTransaction ethSendTransaction = getWeb3j().ethSendRawTransaction(signedTransaction).sendAsync().get();
    if (ethSendTransaction.hasError()) {
      throw new RuntimeException("Error occurred while sending transaction: " + ethSendTransaction.getError().getMessage());
    }
    return ethSendTransaction.getTransactionHash();
  }
}
