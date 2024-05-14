package org.pundi.service.impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;
import org.pundi.common.ResultCode;
import org.pundi.dto.CreateAddressDTO;
import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.entity.UserAddressEntity;
import org.pundi.exception.BusinessRuntimeException;
import org.pundi.mapper.UserAddressMapper;
import org.pundi.service.CurrencyInfoService;
import org.pundi.service.UserService;
import org.pundi.service.UserAddressService;
import org.pundi.util.EthereumUtil;
import org.pundi.util.ObjectMappingUtil;
import org.pundi.vo.CreateAddressVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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

  private final CurrencyInfoService currencyInfoService;

  private final UserService userService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CreateAddressVO createAddress(CreateAddressDTO dto) {
    String asset = dto.getSymbol().toUpperCase();
    Integer networkId = dto.getNetworkId();
    Integer uid = dto.getUid();
    String walletName = dto.getWalletName();

    CurrencyInfoEntity currencyInfoEntity = currencyInfoService.queryByNetworkIdAndAsset(networkId, asset);
    if (Objects.isNull(currencyInfoEntity)) {
      throw new BusinessRuntimeException(ResultCode.CURRENCY_NOT_FOUND);
    }
    List<UserAddressEntity> walletAddressEntityList = queryByUidAndNetwork(networkId, uid);
    if (CollectionUtils.isNotEmpty(walletAddressEntityList)) {
      throw new BusinessRuntimeException(ResultCode.ADDRESS_IS_EXIST);
    }
    Pair<String, String> account = EthereumUtil.createAccount(null);
    UserAddressEntity entity = UserAddressEntity.builder().uid(uid).networkId(networkId).address(account.getLeft()).privateKey(account.getRight())
        .walletName(walletName)
        .asset(asset).build();
    save(entity);

    return ObjectMappingUtil.map(entity, CreateAddressVO.class);
  }

  @Override
  public UserAddressEntity findByNetWorkAssetUid(int network, String asset, Integer uid) {
    LambdaQueryWrapper<UserAddressEntity> wrapper = Wrappers.<UserAddressEntity>lambdaQuery().eq(UserAddressEntity::getNetworkId, network)
        .eq(UserAddressEntity::getAsset, asset).eq(UserAddressEntity::getUid, uid);
    return getOne(wrapper);
  }

  private List<UserAddressEntity> queryByUidAndNetwork(Integer networkId, Integer uid) {
    LambdaQueryWrapper<UserAddressEntity> wrapper = Wrappers.<UserAddressEntity>lambdaQuery().eq(UserAddressEntity::getUid, uid)
        .eq(UserAddressEntity::getNetworkId, networkId);
    return list(wrapper);
  }
}
