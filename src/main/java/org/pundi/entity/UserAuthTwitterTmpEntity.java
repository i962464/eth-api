package org.pundi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * twitter授权信息临时表
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_auth_twitter_tmp")
public class UserAuthTwitterTmpEntity implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 授权token
     */
    @TableField("oauth_token")
    private String oauthToken;

    /**
     * 授权token
     */
    @TableField("oauth_token_secret")
    private String oauthTokenSecret;

    /**
     * 用户token, 授权成功后回填
     */
    @TableField("access_token")
    private String accessToken;

    /**
     * 用户token secret, 授权成功后回填
     */
    @TableField("access_token_secret")
    private String accessTokenSecret;

    /**
     * 登陆token, 授权成功后生成
     */
    @TableField("token")
    private String token;

    /**
     * 是否已关联用户 0:未关联用户; 1:已关联用户
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("dt")
    private Long dt;

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


    public boolean isActive() {
        return status == 1;
    }
}
