package org.pundi.service;

import org.pundi.common.Result;
import org.pundi.dto.twitter.OAuthParamVO;

import java.util.Optional;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年06月17日 14:34:00
 */
public interface TwitterOAuth20Service {

	/**
	 * Authenticate url preparation
	 */
	Result<OAuthParamVO.OAuthPrepareOutput> authPrepareProcess();

	/**
	 * Auth granted process, from Twitter's call back
	 *
	 * @param oauthToken    oauthToken
	 * @param code  code
	 * @return plat access token
	 */
	Optional<String> authGrantProcess(String oauthToken, String code);
}
