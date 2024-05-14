package org.pundi.service;

import org.pundi.entity.CurrencyInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网络-币种映射表 服务类
 * </p>
 *
 * @author 
 * @since 2024-05-11
 */
public interface CurrencyInfoService extends IService<CurrencyInfoEntity> {

  /**
   * 获取币种信息
   * @param networkId
   * @param asset
   * @return
   */
  CurrencyInfoEntity queryByNetworkIdAndAsset(Integer networkId, String asset);

  /**
   * 获取币种信息
   * @param code
   * @param tokenAddress
   * @return
   */
  CurrencyInfoEntity queryByNetworkIdAndContract(Integer code, String tokenAddress);

}
