package org.pundi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.bind.DefaultValue;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Example;
import lombok.Builder.Default;
import lombok.Data;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 09:08:00
 */
@Data
public class CreateAddressDTO {

  /**
   * 用户ID
   */
  @NotNull(message = "用户ID不能为空")
  @ApiModelProperty(notes = "用户ID")
  private Integer uid;

  /**
   * 钱包名称
   */
  @NotBlank(message = "钱包名称不能为空")
  @ApiModelProperty(notes = "钱包名称")
  private String walletName;

  /**
   * 比如 BTC:0 ETH:60
   */
  @NotNull(message = "币种网络ID不能为空")
  @ApiModelProperty(notes = "网络ID", example = "60")
  private Integer networkId;

//  /**
//   * 对应资产(币种),如: ETH
//   */
//  @NotBlank(message = "对应资产(币种)不能为空")
//  @ApiModelProperty(notes = "币种" , example = "ETH")
//  private String symbol;

}
