package org.pundi.service.impl;

import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.mapper.CurrencyInfoMapper;
import org.pundi.service.CurrencyInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 网络-币种映射表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-05-11
 */
@Service
public class CurrencyInfoServiceImpl extends ServiceImpl<CurrencyInfoMapper, CurrencyInfoEntity> implements CurrencyInfoService {

  @Override
  public CurrencyInfoEntity queryByNetworkIdAndAsset(Integer networkId, String symbol) {
    LambdaQueryWrapper<CurrencyInfoEntity> wrapper = Wrappers.<CurrencyInfoEntity>lambdaQuery().eq(CurrencyInfoEntity::getNetworkId, networkId)
        .eq(CurrencyInfoEntity::getSymbol, symbol).eq(CurrencyInfoEntity::getState, 1);
    return getOne(wrapper);
  }

  @Override
  public CurrencyInfoEntity queryByNetworkIdAndContract(Integer networkId, String tokenAddress) {
    LambdaQueryWrapper<CurrencyInfoEntity> wrapper = Wrappers.<CurrencyInfoEntity>lambdaQuery().eq(CurrencyInfoEntity::getNetworkId, networkId)
        .eq(CurrencyInfoEntity::getContractAddress, tokenAddress).eq(CurrencyInfoEntity::getState, 1);
    return getOne(wrapper);  }
}
