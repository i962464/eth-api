package org.pundi.service;

import org.pundi.dto.CreateAddressDTO;
import org.pundi.entity.UserAddressEntity;
import org.pundi.vo.CreateAddressVO;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 钱包充值地址表 服务类
 * </p>
 *
 * @author
 * @since 2024-05-11
 */
public interface UserAddressService extends IService<UserAddressEntity> {

  /**
   * 创建钱包
   * @param dto
   * @return
   */
  CreateAddressVO createAddress(CreateAddressDTO dto);

  /**
   * 查找唯一记录
   * @param network
   * @param uid
   * @return
   */
  UserAddressEntity findByNetWorkAssetUid(int network, String asset, Integer uid);
}