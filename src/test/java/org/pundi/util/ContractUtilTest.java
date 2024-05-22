package org.pundi.util;

import java.math.BigInteger;
import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.pundi.common.ContractBaseParam;
import org.pundi.contract.Erc20TokenContract;
import org.pundi.contract.Erc20TokenContract.ApprovalEventResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月16日 16:35:00
 */
@Slf4j
@SpringBootTest
class ContractUtilTest {

  @Resource
  private ContractUtil contractUtil;

  /**
   * erc20 Token transfer
   * @throws Exception
   */
  @Test
  void erc20TokenTransfer() throws Exception {
    ContractBaseParam param = new ContractBaseParam();
    param.setRpcEndpoint("https://sepolia.infura.io/v3/7cecab00c2b8496cbf7a51fcd05fb5ec");
    param.setContractAddress("0x67550Df3290415611F6C140c81Cd770Ff1742cb9");

    BigInteger gasPrice = EthereumUtil.getGasPrice();
    param.setGasPrice(gasPrice);

    Erc20TokenContract erc20TokenContract = contractUtil.getErc20TokenContract("c9d65fc3529aeab631445e43ba522757415fcfcf6dde380e1daf9362d440e9bd",
        param);
    TransactionReceipt receipt = erc20TokenContract.transfer("0x8ce4092e890c5e21d1596156edc73ab00242b20d", new BigInteger("1"))
        .send();
    System.out.println(receipt.getTransactionHash());
  }

  /**
   * 订阅监听erc20 token 的approve事件
   * @throws Exception
   */
  @Test
  void erc20TokenEvent() throws Exception {
    ContractBaseParam param = new ContractBaseParam();
    param.setRpcEndpoint("https://sepolia.infura.io/v3/7cecab00c2b8496cbf7a51fcd05fb5ec");
    param.setContractAddress("0xEDC0d17D2804D6937dd83b993C7A668059bBF78D");

    Erc20TokenContract erc20TokenContract = contractUtil.getErc20TokenContract(null, param);
    Flowable<ApprovalEventResponse> responseFlowable = erc20TokenContract.approvalEventFlowable(
        new DefaultBlockParameterNumber(BigInteger.valueOf(5945240)),
        new DefaultBlockParameterNumber(BigInteger.valueOf(5945240))
    );

    Disposable subscription = responseFlowable.subscribe(
        event -> {
          String data = event.log.getData();
          byte[] dataBytes = Numeric.hexStringToByteArray(data);

          log.info(">>>data={}", data);
          for (String topic : event.log.getTopics()) {
            log.info(">>>log={}", topic);
          }
        },
        error -> System.err.println("发生错误：" + error),
        () -> System.out.println("订阅完成")
    );
    // 当你不再需要订阅时，可以取消订阅以释放资源
    subscription.dispose();

  }




}