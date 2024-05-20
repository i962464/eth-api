package org.pundi.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.pundi.common.Result;
import org.pundi.dto.CreateAddressDTO;
import org.pundi.dto.EthTransferDTO;
import org.pundi.schedule.WalletJob;
import org.pundi.service.UserAddressService;
import org.pundi.vo.AssetVO;
import org.pundi.vo.CreateAddressVO;
import org.pundi.vo.EtherScanVO;
import org.springframework.boot.test.context.SpringBootTest;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 18:12:00
 */
@SpringBootTest
class WalletAddressControllerTest {

  @Resource
  private UserAddressService walletAddressService;
  @Resource
  private WalletController walletController;
  @Resource
  private WalletJob walletJob;

  @Test
  void createAddress() {
    CreateAddressDTO dto = new CreateAddressDTO();
    dto.setUid(10);
    dto.setWalletName("HD钱包测试2");
    dto.setNetworkId(60);

    Result<CreateAddressVO> address = walletController.createAddress(dto);
    System.out.println(address.toString());
  }

  @Test
  void ethTransfer(){
    EthTransferDTO transferDTO = EthTransferDTO.builder()
        .uid(7)
        .toAddress("0xB25Fdff8D86C85eb5E9b455b71487CE76f086DfF")
        .amount(new BigInteger("2000000000000000"))
        .build();
    Result<String> stringResult = walletController.ethTransfer(transferDTO);
    System.out.println(stringResult);
  }

  @Test
  void tokenTransfer(){
    EthTransferDTO transferDTO = EthTransferDTO.builder()
        .uid(7)
        .tokenAddress("0x67550Df3290415611F6C140c81Cd770Ff1742cb9")
        .toAddress("0xB25Fdff8D86C85eb5E9b455b71487CE76f086DfF")
        .amount(new BigInteger("2000000000000000000"))
        .build();
    Result<String> stringResult = walletController.ethTransfer(transferDTO);
    System.out.println(stringResult);
  }

  @Test
  void ethTxScan() throws IOException, ExecutionException, InterruptedException {
    walletJob.ethTxScan();
  }

  @Test
  void transactions(){
    Result<IPage<EtherScanVO>> aEthDAI = walletController.transactions("aEthDAI", "0xb25fdff8d86c85eb5e9b455b71487ce76f086dff1",
        new BigInteger("5880298"), new BigInteger("5891928"), 1, 10);
    System.out.println(JSONUtil.toJsonStr(aEthDAI));
  }

  @Test
  void getTokensBalance() throws Exception {
    Result<List<AssetVO>> tokensBalance = walletController.getTokensBalance("0x8ce4092e890c5e21d1596156edc73ab00242b20d");
    System.out.println(tokensBalance.toString());
  }

  @Test
  void multipleTransfer() throws IOException, ExecutionException, InterruptedException {
    walletJob.multipleTransfer();
    System.out.println("success");
  }
}