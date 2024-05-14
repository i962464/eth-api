package org.pundi.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 18:34:00
 */
@Data
@Builder
public class EthTransferDTO {


  @NotNull(message = "用户UiD不能为空")
  @ApiModelProperty(notes = "当前用户ID")
  private Integer uid;

  @ApiModelProperty(notes="指定发送的token地址，默认为ETH", example = "0x67550Df3290415611F6C140c81Cd770Ff1742cb9")
  private String tokenAddress;

  @NotBlank(message = "转出地址不能为空")
  @ApiModelProperty(notes = "转出地址")
  private String toAddress;

  @NotNull(message = "转出数量不能为空")
  @ApiModelProperty(notes = "转出ETH数量0.1", example = "100000000000000000")
  private BigInteger amount;


}
