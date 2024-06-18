package org.pundi.service.impl;

import cn.hutool.json.JSONUtil;
import io.gsonfire.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.pundi.common.Result;
import org.pundi.dto.twitter.OAuthGrantResp;
import org.pundi.dto.twitter.OAuthParamVO;
import org.pundi.dto.twitter.OAuthPrepareResp;
import org.pundi.service.TwitterOAuth20Service;
import org.pundi.util.twitter.TwitterOAuthUtil;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年06月17日 16:45:00
 */
@SpringBootTest
class TwitterOAuth20ServiceImplTest {

	@Resource
	private TwitterOAuth20Service twitterOAuth20Service;

	/**
	 * 1-在 OAuth 2.0 授权码流程中，客户端（即您的应用）首先会向授权服务器（如 Twitter）发送一个请求，以获取一个授权 URL。
	 * 		这个 URL 通常会被重定向到用户的浏览器，以便用户登录并授权客户端访问他们的账户信息。
	 * 		一旦用户完成了登录和授权步骤，授权服务器会重定向用户回到客户端之前指定的回调 URL（callbackUrl），并在 URL 中附带一个授权码（authorization code）。
	 * 2-客户端可以使用这个授权码向授权服务器请求访问令牌（access token），该令牌可以用于访问用户的资源。
	 */
	@Test
	void authPrepareProcess() {
		Result<OAuthParamVO.OAuthPrepareOutput> result = twitterOAuth20Service.authPrepareProcess();
		System.out.println(JSONUtil.toJsonStr(result));

	}

}