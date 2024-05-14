package org.pundi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigInteger;

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
@EqualsAndHashCode(callSuper = false)
@TableName("eth_scan_block")
public class EthScanBlockEntity implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("id")
    private Long id;

    /**
     * 扩展网络预留字段
     */
    @TableField("network_id")
    private Integer networkId;

    @TableField("end_block")
    private BigInteger endBlock;


}
