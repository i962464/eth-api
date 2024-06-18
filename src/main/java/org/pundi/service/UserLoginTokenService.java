package org.pundi.service;

import org.pundi.entity.UserLoginTokenEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户登陆token 服务类
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
public interface UserLoginTokenService extends IService<UserLoginTokenEntity> {

	UserLoginTokenEntity findByUserId(Integer userId);
}
