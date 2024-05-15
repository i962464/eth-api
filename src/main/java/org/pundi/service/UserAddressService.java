package org.pundi.service;

import java.util.List;

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
   * 查找唯一记录
   * @param network
   * @param uid
   * @return
   */
  UserAddressEntity findByNetWorkAssetUid(int network, String asset, Integer uid);

  /**
   * 查询列表
   * @param networkId
   * @param uid
   * @return
   */
  List<UserAddressEntity> queryByUidAndNetwork(Integer networkId, Integer uid);

  /**
   * 获取最新的index，HD钱包使用
   * @return
   */
  Integer getLastIndex();
}