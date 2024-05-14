package org.pundi.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 09:56:00
 */
@Data
public class CreateAddressVO {
  /**
   * 比如 BTC:0 ETH:60
   */
  @JsonProperty("networkId")
  @ApiModelProperty(notes = "网络ID")
  private Integer networkId;

  /**
   * 对应资产(币种),如: ETH
   */
  @ApiModelProperty(notes = "币种")
  private String asset;


  @ApiModelProperty(notes = "钱包地址")
  private String address;

  /**
   * eos tag 预留字段
   */
  @ApiModelProperty(notes = "eos tag")
  private String tag;

}
