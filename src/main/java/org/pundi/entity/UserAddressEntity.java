package org.pundi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 钱包充值地址表
 * </p>
 *
 * @author 
 * @since 2024-05-11
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_address")
public class UserAddressEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("uid")
    private Integer uid;

    /**
     * 钱包账户名称
     */
    @TableField("wallet_name")
    private String walletName;

    /**
     * 币种网络ID ，对应 BIP44 分配的 ID
     */
    @TableField("network_id")
    private Integer networkId;

    /**
     * 对应资产(币种),如: ETH
     */
    @TableField("asset")
    private String asset;

    /**
     * 资产地址
     */
    @TableField("address")
    private String address;

    /**
     * todo 暂时写死
     * 钱包私钥
     */
    @TableField("private_key")
    private String privateKey;

    /**
     * Tag/MEMO
     */
    @TableField("tag")
    private String tag;

    /**
     * 状态 1: 正常 0: 禁用
     */
    @TableField("state")
    private Integer state;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

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
