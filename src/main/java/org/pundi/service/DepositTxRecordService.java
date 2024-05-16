package org.pundi.service;

import java.util.ArrayList;
import java.util.List;

import org.pundi.constant.AssetCollectionStatusEnum;
import org.pundi.entity.DepositTxRecordEntity;
import org.pundi.vo.EtherScanVO;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-05-15
 */
public interface DepositTxRecordService extends IService<DepositTxRecordEntity> {

  boolean saveRecord(List<EtherScanVO> depositScans);

  /**
   * 获取未归集记录
   * @param newArrayList
   * @return
   */
  List<DepositTxRecordEntity> getUnCollections(ArrayList<String> newArrayList);

  /**
   * 更新归集状态
   * @param newTxRecord
   * @param hexValue
   * @param statusEnum
   */
  void updateStatusAndHash(List<DepositTxRecordEntity> newTxRecord, String hexValue, AssetCollectionStatusEnum statusEnum);
}
