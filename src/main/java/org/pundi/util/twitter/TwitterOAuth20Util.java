package org.pundi.util.twitter;

import cn.hutool.json.JSONUtil;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.pkce.PKCECodeChallengeMethod;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.auth.TwitterOAuth20Service;
import com.twitter.clientlib.model.Get2UsersMeResponse;
import com.twitter.clientlib.model.User;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pundi.common.Result;
import org.pundi.common.ResultCode;
import org.pundi.dto.twitter.OAuthGrantResp;
import org.pundi.dto.twitter.OAuthPrepareResp;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Twitter OAuth2.0 process util
 *
 * @author Simon
 * @date 2024-06-07 20:22
 **/
@Slf4j
public class TwitterOAuth20Util {

	/**
	 * 获取 oauthAuthenticateUrl
	 *
	 * @param clientId     client id
	 * @param clientSecret client secret
	 * @param callbackUrl  callback url
	 * @param scope        scope
	 * @return OAuthPrepareResp
	 */
	public static Result<OAuthPrepareResp> prepareOauthToken(String clientId, String clientSecret, String callbackUrl, String scope) {
		try (TwitterOAuth20Service oauth20Service = new TwitterOAuth20Service(clientId, clientSecret, callbackUrl, scope)) {
			String codeVerifier = generateCodeVerifier();
			PKCE pkce = getS256Pkce(codeVerifier);
            log.info("codeVerifier: {}, pkce: {}", codeVerifier, JSONUtil.toJsonStr(pkce));

			final String codeChallenge = pkce.getCodeChallenge();
			String authorizationUrl = oauth20Service.getAuthorizationUrl(pkce, codeChallenge);

			return Result.success(
				OAuthPrepareResp.builder()
					.oauthToken(codeChallenge)
					.oauthTokenSecret(codeVerifier)
					.oauthAuthenticateUrl(authorizationUrl)
					.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.error(ResultCode.OAUTH_PREPARE_EXCEPTION);
	}

	/**
	 * 获取用户 token
	 *
	 * @param clientId     client id
	 * @param clientSecret client secret
	 * @param callbackUrl  callback url
	 * @param scope        scope
	 * @param codeVerifier codeVerifier
	 * @param code         code
	 * @return OAuthGrantResp
	 */
	public static Result<OAuthGrantResp> grantOauthAccess(String clientId, String clientSecret, String callbackUrl, String scope, String codeVerifier, String code) {
		try (TwitterOAuth20Service oauth20Service = new TwitterOAuth20Service(clientId, clientSecret, callbackUrl, scope)) {
			AccessTokenRequestParams tokenRequestParams = AccessTokenRequestParams.create(code);
			tokenRequestParams = tokenRequestParams.pkceCodeVerifier(codeVerifier);

			OAuth2AccessToken accessToken = oauth20Service.getAccessToken(tokenRequestParams);
			String token = accessToken.getAccessToken();

//            log.info("accessToken: {}", JSON.toJSONString(accessToken));

			// 计算过期时间戳
			int expires = accessToken.getExpiresIn();
			Instant now = Instant.now();
			Instant expiryTime = now.plus(expires, ChronoUnit.SECONDS);

			String refreshToken = accessToken.getRefreshToken();

			// 获取用户基本信息
			User userTwitterInfo = getUserTwitterInfo(clientId, clientSecret, token, refreshToken);
			log.info("userTwitterInfo: {}", userTwitterInfo);
			String userId = userTwitterInfo.getId();
			String name = userTwitterInfo.getUsername();
			String profileImageUrl = userTwitterInfo.getProfileImageUrl() == null ? "" : userTwitterInfo.getProfileImageUrl().toString();

			return Result.success(OAuthGrantResp.builder()
				.userToken(token)
				.userTokenSecret(codeVerifier)
				.expiryTime(expiryTime.toEpochMilli())
				.refreshToken(refreshToken)
				.profileImageUrl(profileImageUrl)
				.userId(userId)
				.userName(name)
				.build());
		} catch (IOException | ExecutionException | InterruptedException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return Result.error(ResultCode.OAUTH_GRANT_EXCEPTION);
	}

	/**
	 * 获取Twitter 用户信息
	 *
	 * @param clientId     client id
	 * @param clientSecret client secret
	 * @param userToken    用户token
	 * @param refreshToken 用户的刷新token
	 * @return 用户Twitter信息
	 */
	private static User getUserTwitterInfo(String clientId, String clientSecret, String userToken, String refreshToken) {
		TwitterCredentialsOAuth2 twitterCredentialsOAuth2 = new TwitterCredentialsOAuth2(clientId, clientSecret, userToken, refreshToken);
		TwitterApi twitterApi = new TwitterApi(twitterCredentialsOAuth2);
		try {
			Set<String> userFields = new HashSet<>();
			userFields.add("profile_image_url");
			Get2UsersMeResponse usersMeResponse = twitterApi.users().findMyUser().userFields(userFields).execute();

			return usersMeResponse.getData();
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static String generateCodeVerifier() throws Exception {
		SecureRandom secureRandom = new SecureRandom();
		byte[] code = new byte[30];
		secureRandom.nextBytes(code);
		String codeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(code);
		codeVerifier = codeVerifier.replaceAll("[^a-zA-Z0-9]+", "");
		return codeVerifier;
	}

	private static String generateCodeChallenge(String codeVerifier) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(codeVerifier.getBytes("UTF-8"));
		String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
		return codeChallenge;
	}

	@NotNull
	private static PKCE getPlainPkce() {
		PKCE pkce = new PKCE();
		pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
		pkce.setCodeChallenge("codeChallenge");
		pkce.setCodeVerifier("codeChallenge");
		return pkce;
	}

	@NotNull
	private static PKCE getS256Pkce(String codeVerifier) {
		try {
			PKCE pkce = new PKCE();
			pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.S256);
			pkce.setCodeChallenge(generateCodeChallenge(codeVerifier));
			pkce.setCodeVerifier(codeVerifier);
			return pkce;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
