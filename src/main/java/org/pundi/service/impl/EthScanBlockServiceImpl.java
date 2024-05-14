package org.pundi.service.impl;

import java.math.BigInteger;
import java.util.Objects;

import org.pundi.common.ResultCode;
import org.pundi.entity.EthScanBlockEntity;
import org.pundi.mapper.EthScanBlockMapper;
import org.pundi.service.EthScanBlockService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2024-05-13
 */
@Service
public class EthScanBlockServiceImpl extends ServiceImpl<EthScanBlockMapper, EthScanBlockEntity> implements EthScanBlockService {

  @Override
  public EthScanBlockEntity getLastBlock() {
    LambdaQueryWrapper<EthScanBlockEntity> wrapper = Wrappers.<EthScanBlockEntity>lambdaQuery();
    wrapper.last("limit 1");
    return getOne(wrapper);
  }

  @Override
  public void updateBlockNumber(BigInteger blockNumber) {

    LambdaQueryWrapper<EthScanBlockEntity> wrapper = Wrappers.<EthScanBlockEntity>lambdaQuery();
    wrapper.orderByDesc(EthScanBlockEntity::getId).last("limit 1");
    EthScanBlockEntity entity = getOne(wrapper);
    if (Objects.nonNull(entity)) {
      entity.setEndBlock(blockNumber);
      updateById(entity);
    } else {
      throw new RuntimeException(ResultCode.SQL_ERROR.getMsg());
    }
  }
}
