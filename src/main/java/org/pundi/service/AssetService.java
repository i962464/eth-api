package org.pundi.service;

import java.util.List;

import org.pundi.vo.AssetVO;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月17日 11:04:00
 */
public interface AssetService {

  List<AssetVO> queryAssetByAddress(String address) throws Exception;
}
