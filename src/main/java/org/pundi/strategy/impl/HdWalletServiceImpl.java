package org.pundi.strategy.impl;

import java.util.List;
import java.util.Objects;

import org.pundi.common.ResultCode;
import org.pundi.constant.WalletTypeEnum;
import org.pundi.dto.CreateAddressDTO;
import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.entity.UserAddressEntity;
import org.pundi.exception.BusinessRuntimeException;
import org.pundi.service.CurrencyInfoService;
import org.pundi.service.UserAddressService;
import org.pundi.strategy.WalletStrategy;
import org.pundi.util.HDWalletUtil;
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
 * @createTime 2024年05月15日 14:03:00
 */
@Slf4j
@RequiredArgsConstructor
@Component("hd")
public class HdWalletServiceImpl extends BaseAbsServer implements WalletStrategy {

  private final CurrencyInfoService currencyInfoService;
  private final UserAddressService addressService;

  @Override
  public CreateAddressVO createAddress(CreateAddressDTO dto) {

    String asset = dto.getSymbol().toUpperCase();
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
    //获取最新index,如果不存在默认为0
    int index = addressService.getLastIndex();

    String address = HDWalletUtil.createAccount(HDWalletUtil.mnemonic, index);
    UserAddressEntity entity = UserAddressEntity.builder()
        .type(WalletTypeEnum.HD.getCode())
        .uid(uid)
        .networkId(networkId)
        .address(address)
        .hdIndex(index)
        .privateKey("")
        .walletName(walletName)
        .asset(asset).build();
    addressService.save(entity);

    return ObjectMappingUtil.map(entity, CreateAddressVO.class);
  }
}
