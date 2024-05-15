package org.pundi.service.impl;

import java.util.List;
import java.util.Objects;

import org.pundi.entity.UserAddressEntity;
import org.pundi.mapper.UserAddressMapper;
import org.pundi.service.UserAddressService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author
 * @since 2024-05-11
 */
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddressEntity> implements UserAddressService {


  @Override
  public UserAddressEntity findByNetWorkAssetUid(int network, String asset, Integer uid) {
    LambdaQueryWrapper<UserAddressEntity> wrapper = Wrappers.<UserAddressEntity>lambdaQuery().eq(UserAddressEntity::getNetworkId, network)
        .eq(UserAddressEntity::getAsset, asset).eq(UserAddressEntity::getUid, uid);
    return getOne(wrapper);
  }

  @Override
  public List<UserAddressEntity> queryByUidAndNetwork(Integer networkId, Integer uid) {
    LambdaQueryWrapper<UserAddressEntity> wrapper = Wrappers.<UserAddressEntity>lambdaQuery().eq(UserAddressEntity::getUid, uid)
        .eq(UserAddressEntity::getNetworkId, networkId);
    return list(wrapper);
  }

  @Override
  public Integer getLastIndex() {
    LambdaQueryWrapper<UserAddressEntity> wrapper = Wrappers.<UserAddressEntity>lambdaQuery().orderByDesc(UserAddressEntity::getHdIndex)
        .last("limit 1");
    UserAddressEntity entity = getOne(wrapper);
    if (Objects.nonNull(entity)) {
      return Objects.isNull(entity.getHdIndex()) ? 0 : entity.getHdIndex() + 1;
    }
    return 0;
  }
}
