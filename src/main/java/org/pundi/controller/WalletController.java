package org.pundi.controller;


import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.pundi.common.Result;
import org.pundi.constant.WalletTypeEnum;
import org.pundi.dto.CreateAddressDTO;
import org.pundi.dto.EthTransferDTO;
import org.pundi.service.TransferService;
import org.pundi.strategy.WalletStrategy;
import org.pundi.vo.CreateAddressVO;
import org.pundi.vo.EtherScanVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 09:08:00
 */
@Api(tags = "钱包接口", value = "提供钱包相关 Rest API")
@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

  private final TransferService transferService;
  private final Map<String, WalletStrategy> strategyMap;

  /**
   * todo 用户登录，根据用户token 获取用户ID
   * todo 地址私钥暂时保存在表里面， 稍后优化
   * @param dto
   * @return
   */
  @ApiOperation(value = "创建地址", notes = "返回地址")
  @PostMapping("/create/address")
  public Result<CreateAddressVO> createAddress(@Valid @RequestBody CreateAddressDTO dto) {

    //默认使用HD分层钱包
    CreateAddressVO addressVO = strategyMap.get(WalletTypeEnum.getByCode(1).getValue()).createAddress(dto);
    return Result.success(addressVO);
  }

  @ApiOperation(value = "发送交易", notes = "返回hash")
  @PostMapping("/ethTransfer")
  public Result<String> ethTransfer(@Valid @RequestBody EthTransferDTO dto) {

    String txHash = transferService.ethTransfer(dto);
    return Result.success(txHash);
  }


  @ApiOperation(value = "查询链上数据", notes = "")
  @GetMapping("/transactions")
  public Result<IPage<EtherScanVO>> transactions(@RequestParam String symbol,
                                                @RequestParam String address,
                                                @RequestParam BigInteger startBlock,
                                                @RequestParam BigInteger endBlock,
                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

    IPage<EtherScanVO> pageTransactions = transferService.getPageTransactions(symbol, address, startBlock, endBlock, page, pageSize);
    return Result.success(pageTransactions);
  }
}

