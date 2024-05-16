package org.pundi.common;

import java.math.BigInteger;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ekko
 * @version 1.0.0
 * @Description abi2java 调用合约
 * @createTime 2024年05月16日 16:08:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractBaseParam {

  private String rpcEndpoint;

  private Long chainId;

  private String contractAddress;

  private BigInteger gasLimit;

  private BigInteger gasPrice;

  private Map<String, Object> requestData;
}
