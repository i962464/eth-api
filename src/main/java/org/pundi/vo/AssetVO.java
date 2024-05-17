package org.pundi.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月17日 11:07:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetVO {

  @ApiModelProperty(notes = "币种")
  private String token;

  @ApiModelProperty(notes = "余额")
  private String balance;

}
