package org.pundi.service;

import java.math.BigInteger;

import org.pundi.entity.EthScanBlockEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-05-13
 */
public interface EthScanBlockService extends IService<EthScanBlockEntity> {

  EthScanBlockEntity getLastBlock();

  void updateBlockNumber(BigInteger blockNumber);
}
