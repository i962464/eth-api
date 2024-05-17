package org.pundi.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.pundi.common.ContractBaseParam;
import org.pundi.contract.Erc20TokenContract;
import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.service.AssetService;
import org.pundi.service.CurrencyInfoService;
import org.pundi.util.ContractUtil;
import org.pundi.vo.AssetVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月17日 11:05:00
 */
@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

  private final CurrencyInfoService currencyInfoService;
  private final ContractUtil contractUtil;

  @Value("${api.infura.url}")
  private String rpcEndpoint;

  @Override
  public List<AssetVO> queryAssetByAddress(String address) throws Exception {

    List<CurrencyInfoEntity> list = currencyInfoService.list();
    //过滤掉主网币
    List<CurrencyInfoEntity> tokens = list.stream().filter(e -> StringUtils.isNotBlank(e.getContractAddress())).collect(Collectors.toList());

    //构建参数
    ContractBaseParam contractBaseParam = ContractBaseParam.builder().rpcEndpoint(rpcEndpoint).build();
    //结果集
    List<AssetVO> result = Lists.newArrayList();
    for (CurrencyInfoEntity token : tokens) {
      contractBaseParam.setContractAddress(token.getContractAddress());
      Erc20TokenContract erc20TokenContract = contractUtil.getErc20TokenContract(null, contractBaseParam);
      BigInteger balanceBI = erc20TokenContract.balanceOf(address).send();
      String balance = Convert.fromWei(balanceBI.toString(), Unit.ETHER).toPlainString();
      result.add(AssetVO.builder().balance(balance).token(token.getSymbol()).build());
    }
    return result;
  }
}
