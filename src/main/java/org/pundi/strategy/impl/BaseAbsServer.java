package org.pundi.strategy.impl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description 钱包服务抽象基类 公共方法
 * @createTime 2024年05月11日 10:08:00
 */
@Slf4j
public abstract class BaseAbsServer {

  /**
   * 业务通知
   * @param url
   */
  protected boolean doNotify(String url, Object... objects) {

    //todo 业务通知处理
    return true;
  }

}
