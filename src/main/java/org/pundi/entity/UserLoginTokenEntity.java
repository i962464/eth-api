package org.pundi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户登陆token
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_login_token")
public class UserLoginTokenEntity implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 登陆token
     */
    @TableField("token")
    private String token;

    /**
     * ip
     */
    @TableField("ip")
    private String ip;

    /**
     * 创建时间
     */
    @TableField("dt")
    private Long dt;

    /**
     * 更新时间
     */
    @TableField("update_dt")
    private Long updateDt;


}
