package org.pundi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pundi.common.Result;
import org.pundi.dto.twitter.OAuthParamVO;
import org.pundi.service.TwitterOAuth20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @author ekko
 * @version 1.0.0
 * @Description twitter相关测试 文档：https://developer.x.com/en/docs/authentication/oauth-2-0
 * @createTime 2024年06月17日 14:33:00
 */

@Api(tags = "oauth2.0授权模块接口")
@Slf4j
@RestController
@RequestMapping("/oauth20")
@RequiredArgsConstructor
public class TwitterController {

	private final TwitterOAuth20Service twitterOAuth20Service;

//	@NotVerifyLogin
	@ApiOperation(value = "OAuth准备")
	@GetMapping("/prepare")
	public Result<OAuthParamVO.OAuthPrepareOutput> oauthPrepare() {
		return twitterOAuth20Service.authPrepareProcess();
	}


	/**
	 * 点击授权后的回调地址
	 * state 上一步的codeChallenge
	 * @param state
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "OAuth授权")
	@GetMapping("/grant")
	public Result<String> oauthGrant(@RequestParam("state") String state, @RequestParam("code") String code) {
		final Optional<String> opAccessToken = twitterOAuth20Service.authGrantProcess(state, code);
		//        oAuthPrepareOutput.setAuthorizationUrl(oAuthPrepareOutput.getAuthorizationUrl().replaceAll("api.twitter.com", "api.x.com"));
		return Result.success(opAccessToken.get());
	}


//	@ApiOperation(value = "用户激活 (填邀请码)")
//	@ApiImplicitParam(value = "鉴权token", name = "token", paramType = "header", dataType = "String", required = true)
//	@PostMapping(path = "/activeCode", consumes = "application/json", produces = "application/json")
//	public Resp<ActiveUserOutput> userActiveEndpoint(HttpServletRequest request, @Valid @RequestBody ActiveUserInput input,
//													 BindingResult bindingResult) {
//		log.debug(">>> [UserBasicController] userActiveEndpoint input:{}", JSON.toJSONString(input));
//		checkErrors(bindingResult);
//		final ReturnValue<String, RespCode.Default> tokenVal = pureTokenExtract(request);
//		if (tokenVal.hasError()) {
//			return Resp.make(tokenVal.getError());
//		}
//		final ReturnValue<ActiveUserOutput, RespStatusCode> retVal = userBasicService.activeUser(
//			tokenVal.getData(), input.getInvitedCode(), extractIpAddress(request));
//		log.debug("<<< [UserBasicController] userActiveEndpoint output:{}", JSON.toJSONString(retVal));
//		return retVal.hasError() ? Resp.make(retVal.getError()) : Resp.ok(retVal.getData());
//	}

}
