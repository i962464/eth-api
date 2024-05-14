package org.pundi.strategy.impl;

import org.pundi.dto.CreateAddressDTO;
import org.pundi.service.UserAddressService;
import org.pundi.strategy.WalletStrategy;
import org.pundi.vo.CreateAddressVO;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 10:08:00
 */
@Slf4j
@RequiredArgsConstructor
@Component("self")
public class SelfWalletServiceImpl extends BaseAbsServer implements WalletStrategy {

  private final UserAddressService addressService;

  @Override
  public CreateAddressVO createAddress(CreateAddressDTO dto) {

    return addressService.createAddress(dto);
  }
}
