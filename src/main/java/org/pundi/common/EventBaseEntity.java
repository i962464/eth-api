package org.pundi.common;

import lombok.Data;

/**
 * @author ekko
 * @version 1.0.0
 * @Description 事件通知基类
 * @createTime 2024年05月11日 10:10:00
 */
@Data
public class EventBaseEntity<T> {

  /**
   * 事件类型
   *  RECHARGE  充值
   *  WITHDRAW  提现
   *  MPC_SIGN  签名
   *
   */
  private String type;


  /**
   * 具体回调结构体
   */
  private T data;
}
