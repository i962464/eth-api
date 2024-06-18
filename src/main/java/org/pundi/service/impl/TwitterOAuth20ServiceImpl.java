package org.pundi.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pundi.common.Result;
import org.pundi.common.ResultCode;
import org.pundi.config.TwitterConfig;
import org.pundi.dto.twitter.OAuthGrantResp;
import org.pundi.dto.twitter.OAuthParamVO;
import org.pundi.dto.twitter.OAuthPrepareResp;
import org.pundi.entity.UserAuthTwitterEntity;
import org.pundi.entity.UserAuthTwitterTmpEntity;
import org.pundi.entity.UserLoginTokenEntity;
import org.pundi.service.TwitterOAuth20Service;
import org.pundi.service.UserAuthTwitterService;
import org.pundi.service.UserAuthTwitterTmpService;
import org.pundi.service.UserLoginTokenService;
import org.pundi.util.LoginTokenUtil;
import org.pundi.util.twitter.TwitterOAuth20Util;
import org.pundi.util.twitter.TwitterOAuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年06月17日 14:35:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterOAuth20ServiceImpl implements TwitterOAuth20Service {

	private final TwitterConfig twitterConfig;
	private final UserAuthTwitterTmpService userAuthTwitterTmpService;
	private final UserAuthTwitterService userAuthTwitterService;
	private final UserLoginTokenService userLoginTokenService;

	@Resource
	private PlatformTransactionManager transactionManager;

	private String CLIENT_ID;

	private String CLIENT_SECRET;

	private String CALLBACK_URL;

	private String SCOPE;

	@PostConstruct
	public void postConstruct() {
		this.CLIENT_ID = twitterConfig.getClientId();
		this.CLIENT_SECRET = twitterConfig.getClientSecret();
		this.CALLBACK_URL = twitterConfig.getCallbackUrl();
		this.SCOPE = twitterConfig.getScope();
	}

	/**
	 * Authenticate url preparation
	 */
	@Override
	public Result<OAuthParamVO.OAuthPrepareOutput> authPrepareProcess() {

		// construct url
		final Result<OAuthPrepareResp> retVal = TwitterOAuth20Util.prepareOauthToken( CLIENT_ID, CLIENT_SECRET, CALLBACK_URL, SCOPE);
		if (retVal.hasError()) {
			return null;
		}
		// insert into user_auth_twitter_tmp
		final OAuthPrepareResp prepareResp = retVal.getData();
		final UserAuthTwitterTmpEntity entity = buildUserAuthTwitterTmpEntity(prepareResp);

		if (!userAuthTwitterTmpService.save(entity)) {
			return Result.error(ResultCode.PARAM_ERROR);
		}
		return Result.success(
			OAuthParamVO.OAuthPrepareOutput.builder()
				.oauthToken(prepareResp.getOauthToken())
				.authorizationUrl(prepareResp.getOauthAuthenticateUrl())
				.build());
	}

	private UserAuthTwitterTmpEntity buildUserAuthTwitterTmpEntity(OAuthPrepareResp prepareResp) {
		return UserAuthTwitterTmpEntity.builder()
			.userTwitterId("")
			.name("")
			.avatar("")
			.oauthToken(prepareResp.getOauthToken())
			.oauthTokenSecret(prepareResp.getOauthTokenSecret())
			.accessToken("")
			.accessTokenSecret("")
			.expiryTime(0L)
			.refreshToken("")
			.token("")
			.status(0)
			.dt(Instant.now().toEpochMilli())
			.build();
	}


	/**
	 * Auth granted process, from Twitter's call back
	 */
	@Override
	public Optional<String> authGrantProcess(String oauthToken, String code) {

		log.debug("==>authGrantProcess,oauthToken: {}, code: {}", oauthToken, code);
		// 1. oauthToken validation
		UserAuthTwitterTmpEntity opUserAuthTwitterTmp = userAuthTwitterTmpService.findByOauthToken(oauthToken);
		if (Objects.isNull(opUserAuthTwitterTmp)) {
			log.error("<<< [TwitterOAuthServiceImpl] error on authGrantProcess, could not find user_auth_twitter_tmp " +
				"record by oauthToken: {}", oauthToken);
			return Optional.empty();
		}

		//  2. request for access tokens
		String platAccessToken = null;
		UserAuthTwitterTmpEntity userAuthTwitterTmp = opUserAuthTwitterTmp;

		// 2.1 Active User
		if (userAuthTwitterTmp.isActive()) {
			log.debug(">>> [TwitterOAuthServiceImpl] user activated, try update auth token info");
			platAccessToken = activeUserPlatAccessToken(userAuthTwitterTmp);
		}

		// 2.2 Not Active User
		if (!userAuthTwitterTmp.isActive()) {
			// not active, request for user access token
			final Result<OAuthGrantResp> grantRetVal = TwitterOAuth20Util.grantOauthAccess(
				CLIENT_ID, CLIENT_SECRET, CALLBACK_URL, SCOPE, userAuthTwitterTmp.getOauthTokenSecret(), code);
			if (grantRetVal.hasError()) {
				log.error("<<< [TwitterOAuthServiceImpl] error on authGrantProcess, request for user access token failed:{}", grantRetVal.getMsg());
				return Optional.empty();
			}
			final OAuthGrantResp grantResp = grantRetVal.getData();
			log.debug("[TwitterOAuthServiceImpl] success on grantOauthAccess, OAuthGrantResp result:[{}]", JSONUtil.toJsonStr(grantResp));
			opUserAuthTwitterTmp = userAuthTwitterTmpService.findByAccessToken(grantResp.getUserToken());

			// 2.2.2: user call multiple grant
			if (Objects.nonNull(opUserAuthTwitterTmp)) {
				log.debug(">>> [TwitterOAuthServiceImpl] user call multiple grant, update access_token & access_token_secret,id:[{}],accessToken:[{}]", opUserAuthTwitterTmp.getId(), grantResp.getUserToken());

				//查询用户之前是否已经授权并激活过，若存在，则代表此操作是重新授权操作
				final UserAuthTwitterEntity userAuthTwitter = userAuthTwitterService.findByTwitterId(grantResp.getUserId());
				if (Objects.isNull(userAuthTwitter)) {
					log.debug(">>> [TwitterOAuthServiceImpl] user call multiple grant,no user has been bound before");
					// 2.2.2.1 update -> accessToken & accessTokenSecret
					final UserAuthTwitterTmpEntity stored = opUserAuthTwitterTmp;
					userAuthTwitterTmpService.updateAuthTokenAndSecretById(stored.getId(), grantResp.getUserToken(), grantResp.getUserTokenSecret());
					// 2.2.2.2 generate plat access token
					// already active, go find from loginTokenRepo
					// not active yet, use this one
					userAuthTwitterTmp = opUserAuthTwitterTmp;
					platAccessToken = userAuthTwitterTmp.isActive()
						? activeUserPlatAccessToken(userAuthTwitterTmp)
						: userAuthTwitterTmp.getToken();
				} else {
					log.debug(">>> [TwitterOAuthServiceImpl] user call multiple grant,bind user before");
					final UserAuthTwitterTmpEntity stored = opUserAuthTwitterTmp;
					platAccessToken = dealExistUserAuthTwitter(grantResp, stored);
				}
			}

			// 2.2.3: plain new user
			if (Objects.isNull(opUserAuthTwitterTmp)) {
				//先查询该用户之前是否已经授权过并激活，若存在，则代表此操作是重新授权
				final UserAuthTwitterEntity userAuthTwitter = userAuthTwitterService.findByTwitterId(grantResp.getUserId());
				// fills info back & update user_auth_twitter_tmp
				fillUserAccessInfo(userAuthTwitterTmp, grantResp);
				if (Objects.isNull(userAuthTwitter)) {
					log.debug(">>> [TwitterOAuthServiceImpl] plain new user, no user has been bound before");
					//不存在，则是一个新用户
					userAuthTwitterTmpService.updateById(userAuthTwitterTmp);
					platAccessToken = userAuthTwitterTmp.getToken();

				} else {
					//存在，是一个老用户，只是重新授权操作而已
					log.debug(">>> [TwitterOAuthServiceImpl] plain new user, bind user before");
					platAccessToken = dealExistUserAuthTwitter(grantResp, userAuthTwitterTmp);
				}

			}
		}

		return Optional.ofNullable(platAccessToken);
	}

	/**
	 * get active user's plat access token
	 */
	private String activeUserPlatAccessToken(UserAuthTwitterTmpEntity userAuthTwitterTmp) {
		// already active, search for user login token
		final UserAuthTwitterEntity userAuthTwitter = userAuthTwitterService
			.findByAccessToken(userAuthTwitterTmp.getAccessToken()); // must have logically
		final UserLoginTokenEntity userLoginToken = userLoginTokenService
			.findByUserId(userAuthTwitter.getUserId());// must have logically
		return userLoginToken.getToken();
	}

	private String dealExistUserAuthTwitter(OAuthGrantResp grantResp, UserAuthTwitterTmpEntity userAuthTwitterTmp) {

		log.debug(">>> [TwitterOAuthServiceImpl] exists active user start!!!");
		//进行领取积分
		TransactionStatus transactionStatus = null;
		try {
			// 开始手动事务
			transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
			//存在，是一个老用户，只是重新授权操作而已
			final List<UserAuthTwitterTmpEntity> existUserAuthTwitterTmps = userAuthTwitterTmpService.findByUserTwitterIdAndStatus(grantResp.getUserId(), 1);
			log.debug(">>> [TwitterOAuthServiceImpl] exists active user size:[{}]", existUserAuthTwitterTmps.size());
			Set<Integer> ids = existUserAuthTwitterTmps.stream().map(UserAuthTwitterTmpEntity::getId).collect(Collectors.toSet());
			final boolean updateActiveStatus = userAuthTwitterTmpService.updateActiveStatus(ids, 0, 1);
			Preconditions.checkState(updateActiveStatus, "update userAuthTwitterTmp updateActiveStatus fail.");

			//把最新的一条数据改为关联用户，并且更改其余关联表
			//token 沿用之前的token ,以防user_login_token_record 记录表出错
			userAuthTwitterTmp.setToken(existUserAuthTwitterTmps.get(0).getToken());
			log.debug(">>> [TwitterOAuthServiceImpl] exists active user, update userAuthTwitterTmp info:[{}]", JSONUtil.toJsonStr(userAuthTwitterTmp));
			boolean updateUserAccessInfoAndActiveStatus = userAuthTwitterTmpService.updateById(userAuthTwitterTmp);
			Preconditions.checkState(updateUserAccessInfoAndActiveStatus, "update userAuthTwitterTmp updateUserAccessInfoAndActiveStatus fail.");

			UserAuthTwitterEntity updateAuthTwitter = new UserAuthTwitterEntity();
			updateAuthTwitter.setUserTwitterId(grantResp.getUserId());
			updateAuthTwitter.setAccessToken(userAuthTwitterTmp.getAccessToken());
			updateAuthTwitter.setAccessTokenSecret(userAuthTwitterTmp.getAccessTokenSecret());
			updateAuthTwitter.setName(userAuthTwitterTmp.getName());
			updateAuthTwitter.setAvatar(userAuthTwitterTmp.getAvatar());
			updateAuthTwitter.setExpiryTime(0L);
			updateAuthTwitter.setRefreshToken("");
			final boolean updateUserAuthTwitter = userAuthTwitterService.updateUserAuthTwitter(updateAuthTwitter);
			Preconditions.checkState(updateUserAuthTwitter, "update userAuthTwitter updateUserAuthTwitter fail.");
			// 提交事务
			transactionManager.commit(transactionStatus);
			return userAuthTwitterTmp.getToken();
		} catch (Exception e) {
			// 回滚事务
			if (transactionStatus != null) {
				transactionManager.rollback(transactionStatus);
			}
			log.error("<<< [TwitterOAuthServiceImpl] deal exists userAuth twitterId:[{}],error:[{}]", grantResp.getUserId(), e);
			return "";
		}
	}

	public static UserAuthTwitterTmpEntity fillUserAccessInfo(UserAuthTwitterTmpEntity entity, OAuthGrantResp grantResp) {
		entity.setAccessToken(grantResp.getUserToken());
		entity.setAccessTokenSecret(grantResp.getUserTokenSecret());
		entity.setExpiryTime(grantResp.getExpiryTime());
		entity.setRefreshToken(grantResp.getRefreshToken());
		entity.setUserTwitterId(grantResp.getUserId());
		entity.setName("@" + grantResp.getUserName()); // front end need @
		if (grantResp.getProfileImageUrl() != null && !grantResp.getProfileImageUrl().isEmpty()) {
			entity.setAvatar(grantResp.getProfileImageUrl());
		}
//		if (userResp != null && userResp.getData() != null)  {
//			entity.setAvatar(userResp.getData().getProfileImageUrl());
//		}
		entity.setToken(LoginTokenUtil.generateLoginToken(entity.getUserTwitterId()));
		return entity;
	}
}
