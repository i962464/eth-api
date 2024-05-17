package org.pundi.dto;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ekko
 * @version 1.0.0
 * @Description 批量转账使用队列，后期可用MQ优化
 * @createTime 2024年05月17日 15:17:00
 */
@Data
@Builder
@AllArgsConstructor
public class MultipleTransferQueue {

  private Queue<TransferInfo> transactionQueue;

  //初始化queue
  public MultipleTransferQueue(){
    this.transactionQueue = new LinkedList<>();
  }

  public synchronized void addToQueue(TransferInfo transferInfo) {
    transactionQueue.add(transferInfo);
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TransferInfo {
    private String toAddress;
    private BigDecimal amount;
  }
}
