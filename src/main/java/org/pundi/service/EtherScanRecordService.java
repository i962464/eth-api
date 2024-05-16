package org.pundi.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.pundi.entity.EtherScanRecordEntity;
import org.pundi.vo.EtherScanVO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2024-05-14
 */
public interface EtherScanRecordService extends IService<EtherScanRecordEntity> {

  boolean saveRecord(List<EtherScanVO> etherScanVOS);

  IPage<EtherScanVO> pageByParams(String address, String contractAddress, BigInteger startBlock, BigInteger endBlock, Integer page, Integer pageSize);

  List<EtherScanRecordEntity> getBySymbols(ArrayList<String> newArrayList);
}