package org.pundi.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pundi.entity.EtherScanRecordEntity;
import org.pundi.entity.UserEntity;
import org.pundi.mapper.EtherScanRecordMapper;
import org.pundi.service.EtherScanRecordService;
import org.pundi.util.ObjectMappingUtil;
import org.pundi.vo.EtherScanVO;
import org.pundi.vo.UserVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2024-05-14
 */
@Service
public class EtherScanRecordServiceImpl extends ServiceImpl<EtherScanRecordMapper, EtherScanRecordEntity> implements EtherScanRecordService {

  @Override
  public boolean saveRecord(List<EtherScanVO> etherScanVOS) {

    List<EtherScanRecordEntity> records = ObjectMappingUtil.mapAll(etherScanVOS, EtherScanRecordEntity.class);
    return saveBatch(records);
  }

  @Override
  public IPage<EtherScanVO> pageByParams(String address, String contractAddress, BigInteger startBlock, BigInteger endBlock,Integer page, Integer pageSize) {
    LambdaQueryWrapper<EtherScanRecordEntity> wrapper = Wrappers.<EtherScanRecordEntity>lambdaQuery();
    wrapper.ge(EtherScanRecordEntity::getBlockNum, startBlock).le(EtherScanRecordEntity::getBlockNum, endBlock);
    wrapper.and(wp -> wp.eq(EtherScanRecordEntity::getReceiverAddress, address).or().eq(EtherScanRecordEntity::getSenderAddress, address));
    if(StringUtils.isNotBlank(contractAddress)){
      wrapper.eq(EtherScanRecordEntity::getTokenAddress, contractAddress);
    }else{
      wrapper.isNull(EtherScanRecordEntity::getTokenAddress);
    }

    IPage<EtherScanRecordEntity> pageResult = page(new Page<>(page, pageSize), wrapper);
    return pageResult.convert(e -> ObjectMappingUtil.map(e, EtherScanVO.class));
  }

  @Override
  public List<EtherScanRecordEntity> getBySymbols(ArrayList<String> newArrayList) {
    LambdaQueryWrapper<EtherScanRecordEntity> wrapper = Wrappers.<EtherScanRecordEntity>lambdaQuery();
    wrapper.in(EtherScanRecordEntity::getTokenName);
    return list(wrapper);
  }
}
