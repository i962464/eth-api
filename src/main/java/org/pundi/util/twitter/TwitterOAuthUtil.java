package org.pundi.util.twitter;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import lombok.extern.slf4j.Slf4j;
import org.pundi.common.Result;
import org.pundi.common.ResultCode;
import org.pundi.dto.twitter.OAuthGrantResp;
import org.pundi.dto.twitter.OAuthPrepareResp;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Twitter OAuth process util
 *
 * @author Roylic
 * 2024/1/24
 */
@Slf4j
public class TwitterOAuthUtil {

	/**
	 * OAuth 1.0a - Step 1
	 *
	 * @param consumerKey    consumer key
	 * @param consumerSecret consumer secret
	 * @param callbackUrl    callback url
	 * @return OAuthPrepareResp
	 */
	public static Result<OAuthPrepareResp> prepareOauthToken(String consumerKey, String consumerSecret,
															 String callbackUrl) {
		final OAuth10aService oauthService = new ServiceBuilder(consumerKey)
			.apiSecret(consumerSecret)
			.callback(callbackUrl)
			.build(TwitterApi.instance());
		try {
			final OAuth1RequestToken requestToken = oauthService.getRequestToken();
			return Result.success(
				OAuthPrepareResp.builder()
					.oauthToken(requestToken.getToken())
					.oauthTokenSecret(requestToken.getTokenSecret())
					.oauthAuthenticateUrl(
						oauthService.getAuthorizationUrl(requestToken)
							.replace("authorize", "authenticate")) // use authenticate
					.build());
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return Result.error(ResultCode.OAUTH_PREPARE_EXCEPTION);
	}

	/**
	 * OAuth 1.0a - Step 2
	 *
	 * @param consumerKey      consumer key
	 * @param consumerSecret   consumer secret
	 * @param oauthToken       token
	 * @param oauthTokenSecret token secret
	 * @param oauthVerifier    7 ~ 9 digit verifier
	 * @return OAuthGrantResp
	 */
	public static Result<OAuthGrantResp> grantOauthAccess(String consumerKey, String consumerSecret,
														  String oauthToken, String oauthTokenSecret, String oauthVerifier) {
		final OAuth10aService oauthService = new ServiceBuilder(consumerKey)
			.apiSecret(consumerSecret)
			.build(TwitterApi.instance());
		try {
			final OAuth1AccessToken accessToken = oauthService.getAccessToken(
				new OAuth1RequestToken(oauthToken, oauthTokenSecret), oauthVerifier);
			return Result.success(OAuthGrantResp.builder()
				.userToken(accessToken.getParameter("oauth_token"))
				.userTokenSecret(accessToken.getParameter("oauth_token_secret"))
				.userId(accessToken.getParameter("user_id"))
				.userName(accessToken.getParameter("screen_name"))
				.expiryTime(0)
				.refreshToken("")
				.build());
		} catch (IOException | InterruptedException | ExecutionException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return Result.error(ResultCode.OAUTH_GRANT_EXCEPTION);
	}

}
