package org.pundi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.pundi.entity.EthScanBlockEntity;
import org.pundi.entity.UserAuthTwitterEntity;
import org.pundi.entity.UserAuthTwitterTmpEntity;
import org.pundi.mapper.UserAuthTwitterTmpMapper;
import org.pundi.service.UserAuthTwitterTmpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * twitter授权信息临时表 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Service
public class UserAuthTwitterTmpServiceImpl extends ServiceImpl<UserAuthTwitterTmpMapper, UserAuthTwitterTmpEntity> implements UserAuthTwitterTmpService {

	@Override
	public UserAuthTwitterTmpEntity findByOauthToken(String oauthToken) {

		LambdaQueryWrapper<UserAuthTwitterTmpEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserAuthTwitterTmpEntity::getOauthToken, oauthToken);
		return getOne(wrapper);
	}

	@Override
	public UserAuthTwitterTmpEntity findByAccessToken(String userToken) {
		LambdaQueryWrapper<UserAuthTwitterTmpEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserAuthTwitterTmpEntity::getAccessToken, userToken);
		return getOne(wrapper);
	}

	@Override
	public boolean updateAuthTokenAndSecretById(Integer id, String userToken, String userTokenSecret) {
		LambdaUpdateWrapper<UserAuthTwitterTmpEntity> wrapper = Wrappers.lambdaUpdate();
		wrapper.set(UserAuthTwitterTmpEntity::getAccessToken, userToken)
			.set(UserAuthTwitterTmpEntity::getAccessTokenSecret, userTokenSecret)
			.eq(UserAuthTwitterTmpEntity::getId, id);
		return update(wrapper);
	}

	@Override
	public List<UserAuthTwitterTmpEntity> findByUserTwitterIdAndStatus(String userId, int status) {
//		return select().where(Conditions.equals("user_twitter_id", userTwitterId).and(Conditions.equals("status", status))).find();

		LambdaQueryWrapper<UserAuthTwitterTmpEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserAuthTwitterTmpEntity::getUserTwitterId, userId).eq(UserAuthTwitterTmpEntity::getStatus, status);
		return list(wrapper);
	}

	@Override
	public boolean updateActiveStatus(Set<Integer> ids, int status, int oldStatus) {


		LambdaUpdateWrapper<UserAuthTwitterTmpEntity> wrapper = Wrappers.lambdaUpdate();
		wrapper.set(UserAuthTwitterTmpEntity::getStatus, status)
			.in(UserAuthTwitterTmpEntity::getUserTwitterId, ids)
			.eq(UserAuthTwitterTmpEntity::getStatus, oldStatus);
		return update(wrapper);
	}


}
