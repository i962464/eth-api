package org.pundi.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.entity.UserTransactionsEntity;
import org.pundi.mapper.UserTransactionsMapper;
import org.pundi.service.UserTransactionsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2024-05-13
 */
@Service
public class UserTransactionsServiceImpl extends ServiceImpl<UserTransactionsMapper, UserTransactionsEntity> implements UserTransactionsService {

  @Override
  public boolean saveRecord(Integer uid, String address, String toAddress, BigDecimal amount, CurrencyInfoEntity currencyInfo,
                            String txHash) {
    return save(UserTransactionsEntity.builder()
        .uid(uid)
        .senderAddress(address)
        .receiverAddress(toAddress)
        .amount(amount)
        .tokenName(currencyInfo.getSymbol())
        .tokenAddress(currencyInfo.getContractAddress())
        .transactionHash(txHash).build());
  }
}
