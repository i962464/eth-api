//package org.pundi.util.twitter;
//
//import com.alibaba.fastjson.JSON;
//import com.github.scribejava.apis.TwitterApi;
//import com.github.scribejava.core.builder.ServiceBuilder;
//import com.github.scribejava.core.model.OAuth1AccessToken;
//import com.github.scribejava.core.model.OAuthRequest;
//import com.github.scribejava.core.model.Response;
//import com.github.scribejava.core.model.Verb;
//import com.github.scribejava.core.oauth.OAuth10aService;
//import com.wokoworks.framework.data.ReturnValue;
//import com.wokoworks.xplugin.common.constants.TwitterErrCodes;
//import com.wokoworks.xplugin.common.dto.twitter.resp.UserTweetsResp;
//import com.wokoworks.xplugin.common.utils.XpluginTimeUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.util.StringUtils;
//
//import java.io.IOException;
//import java.util.concurrent.ExecutionException;
//
///**
// * Twitter request (with User oauth) util
// *
// * @author Roylic
// * 2024/1/30
// */
//@Slf4j
//public class TwitterUserRequestUtil {
//
//    private static final String BASE_URL = "https://api.x.com/2/";
//
//
//    public static ReturnValue<UserTweetsResp, TwitterErrCodes> getUserPeriodTweets(String consumerK, String consumerS,
//                                                                                   String userId, String userAccessToken, String userAccessTokenS,
//                                                                                   long startTs, long endTs, String lastCheckTweetId) {
//        // OAuth1.0a
//        OAuth10aService auth10aService = new ServiceBuilder(consumerK)
//                .apiSecret(consumerS)
//                .build(TwitterApi.instance());
//
//        String url = BASE_URL + "users/" + userId + "/tweets";
//
//        OAuthRequest request = new OAuthRequest(Verb.GET, url);
//        // request.addQuerystringParameter("exclude", "retweets,replies");
//        request.addQuerystringParameter("exclude", "retweets");
//        request.addQuerystringParameter("max_results", "100");
//        request.addQuerystringParameter("start_time", XpluginTimeUtils.ts2Date(startTs));
//        request.addQuerystringParameter("end_time", XpluginTimeUtils.ts2Date(endTs));
//        request.addQuerystringParameter("tweet.fields", "created_at");
//        if (StringUtils.hasLength(lastCheckTweetId)) {
//            request.addQuerystringParameter("since_id", lastCheckTweetId);
//        }
//        request.addHeader("Content-Type", "application/json");
//
//        log.debug(">>> [TwitterUserRequestUtil] get request on url:{}", request.getCompleteUrl());
//        auth10aService.signRequest(
//                new OAuth1AccessToken(userAccessToken, userAccessTokenS), request);
//
//        TwitterErrCodes errCodes = TwitterErrCodes.DEFAULT;
//        try {
//            final Response resp = auth10aService.execute(request);
//            final String body = resp.getBody();
//            log.debug("<<< [TwitterUserRequestUtil] response:{}", body);
//
//            // http code 200
//            if (resp.isSuccessful()) {
//                return ReturnValue.withOk(JSON.parseObject(body, UserTweetsResp.class));
//            }
//
//            // not 200
//            final int code = resp.getCode();
//            errCodes = TwitterErrCodes.findByStatusCode(code);
//
//        } catch (InterruptedException | ExecutionException | IOException e) {
//            e.printStackTrace();
//        }
//        return ReturnValue.withError(errCodes);
//    }
//
//}
