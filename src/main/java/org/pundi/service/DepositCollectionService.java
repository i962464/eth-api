package org.pundi.service;

import java.io.IOException;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月16日 09:53:00
 */
public interface DepositCollectionService {

  /**
   * 1-使用HD钱包创建地址
   * 2-拉去链上充值记录
   * 3-根据最新区块确认数-交易所在区块 + 1 >12 判断交易是否确认
   * 4-判断当前充值地址是否存在足够的gas
   * 5-如果不存在，钱包转手续费给充值地址
   * 6-调用离线服务构建交易
   * 7-调用离线服务签名，并缓存到redis
   * 8-获取交易的签名列表，广播交易
   * 9-获取广播交易hash，更新归集状态
   * @throws IOException
   */
  void depositCollection() throws IOException;
}
