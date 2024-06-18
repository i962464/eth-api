package org.pundi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.pundi.entity.UserAuthTwitterEntity;
import org.pundi.mapper.UserAuthTwitterMapper;
import org.pundi.service.UserAuthTwitterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户twitter授权信息 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Service
public class UserAuthTwitterServiceImpl extends ServiceImpl<UserAuthTwitterMapper, UserAuthTwitterEntity> implements UserAuthTwitterService {

	@Override
	public UserAuthTwitterEntity findByAccessToken(String accessToken) {
		LambdaQueryWrapper<UserAuthTwitterEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserAuthTwitterEntity::getAccessToken, accessToken);
		return getOne(wrapper);
	}

	@Override
	public UserAuthTwitterEntity findByTwitterId(String userTwitterId) {

		LambdaQueryWrapper<UserAuthTwitterEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserAuthTwitterEntity::getUserTwitterId, userTwitterId);
		return getOne(wrapper);
	}

	@Override
	public boolean updateUserAuthTwitter(UserAuthTwitterEntity userAuthTwitter) {
		LambdaUpdateWrapper<UserAuthTwitterEntity> wrapper = Wrappers.lambdaUpdate();
		wrapper.set(UserAuthTwitterEntity::getAccessToken, userAuthTwitter.getAccessToken())
			.set(UserAuthTwitterEntity::getAccessTokenSecret, userAuthTwitter.getAccessTokenSecret())
			.set(UserAuthTwitterEntity::getName, userAuthTwitter.getName())
			.set(UserAuthTwitterEntity::getAvatar, userAuthTwitter.getAvatar())
			.set(UserAuthTwitterEntity::getExpiryTime, userAuthTwitter.getExpiryTime())
			.set(UserAuthTwitterEntity::getRefreshToken, userAuthTwitter.getRefreshToken())
			.set(UserAuthTwitterEntity::getUpdateDt, System.currentTimeMillis())
			.eq(UserAuthTwitterEntity::getUserTwitterId, userAuthTwitter.getUserTwitterId());
		return update(wrapper);
	}
}
