package org.pundi.schedule;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.pundi.service.DepositCollectionService;
import org.pundi.service.TransferService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月13日 12:18:00
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WalletJob {

  private final TransferService transferService;
  private final DepositCollectionService collectionService;

  /**
   * 5 分钟一次网络交易扫描(解析ETH、ERC20 Token 交易)
   */
//  @Scheduled(fixedRate = 1000 * 60 * 5, initialDelay = 1000 * 30)
  public void ethTxScan() throws IOException, ExecutionException, InterruptedException {
    log.info("ethTxScan start ...");
    transferService.scanUserTxs();
    log.info("ethTxScan end ...");
  }

  /**
   * 充值归集 https://sepolia.etherscan.io/address/0x8ce4092e890c5e21d1596156edc73ab00242b20d
   */
//  @Scheduled(fixedRate = 1000 * 60 * 5, initialDelay = 1000 * 30)
  public void depositTxScan() throws IOException {
    log.info("depositCollection start ...");

    collectionService.depositCollection();
    log.info("depositCollection end ...");
  }

  /**
   * 批量转账
   * @throws IOException
   */
//  @Scheduled(fixedRate = 1000 * 60 * 5, initialDelay = 1000 * 30)
  public void multipleTransfer() throws IOException, ExecutionException, InterruptedException {

    transferService.multipleTransfer();
  }


}
