package org.pundi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.pundi.constant.AssetCollectionStatusEnum;
import org.pundi.entity.DepositTxRecordEntity;
import org.pundi.mapper.DepositTxRecordMapper;
import org.pundi.service.DepositTxRecordService;
import org.pundi.util.ObjectMappingUtil;
import org.pundi.vo.EtherScanVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2024-05-15
 */
@Service
public class DepositTxRecordServiceImpl extends ServiceImpl<DepositTxRecordMapper, DepositTxRecordEntity> implements DepositTxRecordService {

  @Override
  public boolean saveRecord(List<EtherScanVO> depositScans) {

    List<DepositTxRecordEntity> list = ObjectMappingUtil.mapAll(depositScans, DepositTxRecordEntity.class);
    return saveBatch(list);
  }

  @Override
  public List<DepositTxRecordEntity> getUnCollections(ArrayList<String> symbol) {
    LambdaQueryWrapper<DepositTxRecordEntity> wrapper = Wrappers.lambdaQuery();
    wrapper.in(CollectionUtils.isNotEmpty(symbol), DepositTxRecordEntity::getTokenName, symbol)
        .eq(DepositTxRecordEntity::getCollectionStatus, AssetCollectionStatusEnum.UNCOLLECTED.getCode());
    return list(wrapper);
  }

  @Override
  public void updateStatusAndHash(List<DepositTxRecordEntity> newTxRecord, String hexValue, AssetCollectionStatusEnum statusEnum) {

  }
}
