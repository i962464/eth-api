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
 * 用户twitter授权信息
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_auth_twitter")
public class UserAuthTwitterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 用户的twitter id
     */
    @TableField("user_twitter_id")
    private String userTwitterId;

    /**
     * twitter用户名
     */
    @TableField("name")
    private String name;

    /**
     * twitter头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用户token
     */
    @TableField("access_token")
    private String accessToken;

    /**
     * 用户token secret
     */
    @TableField("access_token_secret")
    private String accessTokenSecret;

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

    /**
     * token过期时间
     */
    @TableField("expiry_time")
    private Long expiryTime;

    /**
     * 刷新token
     */
    @TableField("refresh_token")
    private String refreshToken;


}
