package org.pundi.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("deposit_tx_record")
public class DepositTxRecordEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @TableField("uid")

  private Integer uid;

  @TableField("block_num")
  private BigInteger blockNum;

  @TableField("block_hash")
  private String blockHash;

  @TableField("sender_address")
  private String senderAddress;

  @TableField("receiver_address")
  private String receiverAddress;

  @TableField("amount")
  private BigInteger amount;

  @TableField("token_name")
  private String tokenName;

  @TableField("token_address")
  private String tokenAddress;

  @TableField("transaction_hash")
  private String transactionHash;

  /**
   * 0-未归集， 1-归集中， 2-已归集
   */
  @TableField("collection_status")
  private Integer collectionStatus;

  /**
   * 乐观锁预留
   */
  @TableField("version")
  private Integer version;

  /**
   * 逻辑删除，冗余字段
   */
  @TableField("deleted")
  private Boolean deleted;

  @TableField("deleted_at")
  private LocalDateTime deletedAt;

  @TableField("updated_at")
  private LocalDateTime updatedAt;

  @TableField("created_at")
  private LocalDateTime createdAt;


}
