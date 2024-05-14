package org.pundi.schedule;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.pundi.service.TransferService;
import org.pundi.service.UserTransactionsService;
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
  /**
   * 5 分钟一次网络交易扫描
   */
  @Scheduled(fixedRate = 1000 * 60 * 5, initialDelay = 1000 * 30)
  public void ethTxScan() throws IOException, ExecutionException, InterruptedException {
    log.info("ethTxScan start ...");
    transferService.scanUserTxs();
    log.info("ethTxScan end ...");
  }
}
