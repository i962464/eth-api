package org.pundi.service;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.pundi.entity.CurrencyInfoEntity;
import org.pundi.entity.UserTransactionsEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-05-13
 */
public interface UserTransactionsService extends IService<UserTransactionsEntity> {

  boolean saveRecord(Integer uid, String address, String toAddress, BigDecimal amount, CurrencyInfoEntity currencyInfo, String txHash);
}
