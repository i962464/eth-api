package org.pundi.strategy.impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;
import org.pundi.common.ResultCode;
import org.pundi.constant.NetworkEnum;
import org.pundi.dto.CreateAddressDTO;
import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.entity.UserAddressEntity;
import org.pundi.exception.BusinessRuntimeException;
import org.pundi.service.CurrencyInfoService;
import org.pundi.service.UserAddressService;
import org.pundi.strategy.WalletStrategy;
import org.pundi.util.EthereumUtil;
import org.pundi.util.ObjectMappingUtil;
import org.pundi.vo.CreateAddressVO;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 10:08:00
 */
@Slf4j
@RequiredArgsConstructor
@Component("self")
public class SelfWalletServiceImpl extends BaseAbsServer implements WalletStrategy {

  private final UserAddressService addressService;
  private final CurrencyInfoService currencyInfoService;
  @Override
  public CreateAddressVO createAddress(CreateAddressDTO dto) {

    String asset = NetworkEnum.getByCode(dto.getNetworkId()).getValue().toUpperCase();
    Integer networkId = dto.getNetworkId();
    Integer uid = dto.getUid();
    String walletName = dto.getWalletName();

    CurrencyInfoEntity currencyInfoEntity = currencyInfoService.queryByNetworkIdAndAsset(networkId, asset);
    if (Objects.isNull(currencyInfoEntity)) {
      throw new BusinessRuntimeException(ResultCode.CURRENCY_NOT_FOUND);
    }
    List<UserAddressEntity> walletAddressEntityList = addressService.queryByUidAndNetwork(networkId, uid);
    if (CollectionUtils.isNotEmpty(walletAddressEntityList)) {
      throw new BusinessRuntimeException(ResultCode.ADDRESS_IS_EXIST);
    }
    Pair<String, String> account = EthereumUtil.createAccount(null);
    UserAddressEntity entity = UserAddressEntity.builder().uid(uid).networkId(networkId).address(account.getLeft()).privateKey(account.getRight())
        .walletName(walletName)
        .asset(asset).build();
    addressService.save(entity);

    return ObjectMappingUtil.map(entity, CreateAddressVO.class);

  }
}
