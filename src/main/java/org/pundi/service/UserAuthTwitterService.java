package org.pundi.service;

import org.pundi.entity.UserAuthTwitterEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户twitter授权信息 服务类
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
public interface UserAuthTwitterService extends IService<UserAuthTwitterEntity> {

	UserAuthTwitterEntity findByAccessToken(String accessToken);

	UserAuthTwitterEntity findByTwitterId(String userId);

	boolean updateUserAuthTwitter(UserAuthTwitterEntity updateAuthTwitter);
}
