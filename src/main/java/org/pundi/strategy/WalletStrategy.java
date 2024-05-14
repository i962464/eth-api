package org.pundi.strategy;

import org.pundi.dto.CreateAddressDTO;
import org.pundi.vo.CreateAddressVO;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 09:56:00
 */
public interface WalletStrategy {

  /**
   * 创建地址
   * @param data
   * @return
   */
  CreateAddressVO createAddress(CreateAddressDTO data);
}
