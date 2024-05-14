package org.pundi.vo;

import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月14日 07:41:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtherScanVO {

  @ApiModelProperty(notes = "区块号")
  private BigInteger blockNum;

  @ApiModelProperty(notes = "区块hash")
  private String blockHash;

  @ApiModelProperty(notes = "from地址")
  private String senderAddress;

  @ApiModelProperty(notes = "to地址")
  private String receiverAddress;

  @ApiModelProperty(notes = "数量")
  private BigInteger amount;

  @ApiModelProperty(notes = "币种名称")
  private String tokenName;

  @ApiModelProperty(notes = "币种地址")
  private String tokenAddress;

  @ApiModelProperty(notes = "交易hash")
  private String transactionHash;
}
