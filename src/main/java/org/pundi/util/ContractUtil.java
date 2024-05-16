package org.pundi.util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.pundi.common.ContractBaseParam;
import org.pundi.contract.Erc20TokenContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月16日 16:10:00
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ContractUtil {

  @Autowired
  private OkHttpClient okHttpClient;

  private ContractGasProvider getContractGasProvider(ContractBaseParam baseParam) {
    if (baseParam.getGasLimit() != null && baseParam.getGasPrice() != null) {
      return new StaticGasProvider(baseParam.getGasPrice(), baseParam.getGasLimit());
    }
    Web3j web3j = Web3j.build(new HttpService(baseParam.getRpcEndpoint(),okHttpClient));
    try {
      BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
      return new StaticGasProvider(gasPrice, DefaultGasProvider.GAS_LIMIT);
    } catch (IOException e) {
      log.error("ethGasPrice error{}",e);
    }
    return null;
  }


  /**
   * 初始化合约对象
   * @param privateKey
   * @param baseParam
   * @return
   */
  public Erc20TokenContract getErc20TokenContract(String privateKey, ContractBaseParam baseParam) {
    if(StringUtils.isBlank(privateKey)){
      //读合约可以不传私钥， 这里从配置写死只为获取Credentials对象
      privateKey = "7b29a189fc90cec6cd58c4c12196273d3c7408da1c47a638a4c73c42ff328d60";
    }
    Credentials credentials = Credentials.create(privateKey);
//    ContractGasProvider gasProvider = getContractGasProvider(baseParam);
    ContractGasProvider gasProvider = new DefaultGasProvider();


    Long chainId = baseParam.getChainId();
    Web3j web3j = Web3j.build(new HttpService(baseParam.getRpcEndpoint(),okHttpClient));
    if (Objects.isNull(chainId)) {
      return Erc20TokenContract.load(baseParam.getContractAddress(), web3j, credentials, gasProvider);
    }
    TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, chainId, 1, 1000);
    return Erc20TokenContract.load(baseParam.getContractAddress(), web3j, transactionManager, gasProvider);
  }


}
