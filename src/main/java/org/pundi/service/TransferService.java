package org.pundi.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.pundi.dto.EthTransferDTO;
import org.pundi.vo.EtherScanVO;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 18:48:00
 */
public interface TransferService {

  /**
   * eth转出
   * @param dto
   * @return
   */
  String ethTransfer(EthTransferDTO dto);

  /**
   * 扫描用户交易记录
   */
  void scanUserTxs() throws IOException, InterruptedException, ExecutionException;

  /**
   * 查询链上记录
   * @param symbol
   * @param address
   * @param startBlock
   * @param endBlock
   * @param page
   * @param pageSize
   * @return
   */
  IPage<EtherScanVO> getPageTransactions(String symbol, String address, BigInteger startBlock, BigInteger endBlock, Integer page, Integer pageSize);
}
