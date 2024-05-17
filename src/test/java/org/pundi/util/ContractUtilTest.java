package org.pundi.util;

import java.math.BigInteger;
import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.pundi.common.ContractBaseParam;
import org.pundi.contract.Erc20TokenContract;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月16日 16:35:00
 */
@SpringBootTest
class ContractUtilTest {

  @Resource
  private ContractUtil contractUtil;

  @Test
  void getErc20TokenContract() throws Exception {
    ContractBaseParam param = new ContractBaseParam();
    param.setRpcEndpoint("https://sepolia.infura.io/v3/7cecab00c2b8496cbf7a51fcd05fb5ec");
//    param.setChainId();
    param.setContractAddress("0x67550Df3290415611F6C140c81Cd770Ff1742cb9");
//    param.setGasLimit();
//    param.setGasPrice();
//    param.setRequestData();

    BigInteger gasPrice = EthereumUtil.getGasPrice();
    param.setGasPrice(gasPrice);

    Erc20TokenContract erc20TokenContract = contractUtil.getErc20TokenContract("c9d65fc3529aeab631445e43ba522757415fcfcf6dde380e1daf9362d440e9bd",
        param);
    TransactionReceipt receipt = erc20TokenContract.transfer("0x8ce4092e890c5e21d1596156edc73ab00242b20d", new BigInteger("1"))
        .send();
    System.out.println(receipt.getTransactionHash());

//    pendinghash:0x41c77e83d40dd33f1cf57278c0cbeb30062cd6b79c9d4edb868798a7e6d9e65c

  }


}