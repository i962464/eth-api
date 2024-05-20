package org.pundi.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.pundi.common.BaseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2024-05-13
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_transactions")
public class UserTransactionsEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @TableField("uid")
  private Integer uid;

  @TableField("sender_address")
  private String senderAddress;

  @TableField("receiver_address")
  private String receiverAddress;

  @TableField("amount")
  private BigDecimal amount;

  @TableField("token_name")
  private String tokenName;

  @TableField("token_address")
  private String tokenAddress;

  @TableField("transaction_hash")
  private String transactionHash;


}
