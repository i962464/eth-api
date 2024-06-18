package org.pundi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.pundi.entity.UserLoginTokenEntity;
import org.pundi.mapper.UserLoginTokenMapper;
import org.pundi.service.UserLoginTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登陆token 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Service
public class UserLoginTokenServiceImpl extends ServiceImpl<UserLoginTokenMapper, UserLoginTokenEntity> implements UserLoginTokenService {

	@Override
	public UserLoginTokenEntity findByUserId(Integer userId) {
		LambdaQueryWrapper<UserLoginTokenEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserLoginTokenEntity::getUserId, userId);
		return getOne(wrapper);
	}
}
