package org.pundi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 网络-币种映射表
 * </p>
 *
 * @author 
 * @since 2024-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("currency_info")
public class CurrencyInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 区块链网络ID
     */
    @TableField("network_id")
    private Integer networkId;

    /**
     * 对应资产(币种),如: ETH
     */
    @TableField("symbol")
    private String symbol;

    /**
     * token 名称
     */
    @TableField("name")
    private String name;


    /**
     * 1 可用 0 冻结
     */
    @TableField("state")
    private Integer state;

    /**
     * erc20合约地址
     */
    @TableField("contract_address")
    private String contractAddress;

    /**
     * 币种精度
     */
    @TableField("decimals")
    private Integer decimals;

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
