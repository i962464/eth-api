//package org.pundi.util.twitter;
//
//import com.alibaba.fastjson2.JSON;
//import com.wokoworks.framework.data.ReturnValue;
//import com.wokoworks.xplugin.common.constants.TwitterErrCodes;
//import com.wokoworks.xplugin.common.dto.twitter.resp.*;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.HttpUrl;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.util.StringUtils;
//
//import java.io.IOException;
//
///**
// * Twitter request util
// *
// * @author Roylic
// * 2024/1/24
// */
//@Slf4j
//public class TwitterRequestUtil {
//
//    private static final String BASE_URL = "https://api.x.com/2";
//
//
//    /**
//     * 获取转发了某条tweet的所有用户id (分页)
//     *
//     * @param client          okhttp
//     * @param bearer          bearer token
//     * @param tweeterId       tweeter id
//     * @param paginationToken 上一条的nextToken
//     * @return RetweetUserResp
//     */
//    public static ReturnValue<RetweetUserResp, TwitterErrCodes> getRetweetUsers(OkHttpClient client, String bearer,
//                                                                                String tweeterId, String paginationToken) {
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
//        urlBuilder.addPathSegment("tweets")
//                .addPathSegment(tweeterId)
//                .addPathSegment("retweeted_by");
//        if (StringUtils.hasLength(paginationToken)) {
//            urlBuilder.addQueryParameter("pagination_token", paginationToken);
//        }
//        return executeGet(client, bearer, urlBuilder.build(), RetweetUserResp.class);
//    }
//
//    /**
//     * 获取点赞了某条tweet的所有用户id (分页)
//     *
//     * @param client          okhttp
//     * @param bearer          bearer token
//     * @param tweeterId       tweeter id
//     * @param paginationToken 上一条的nextToken
//     * @return LikingUserResp
//     */
//    public static ReturnValue<LikingUserResp, TwitterErrCodes> getLikingUsers(OkHttpClient client, String bearer,
//                                                                              String tweeterId, String paginationToken) {
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
//        urlBuilder.addPathSegment("tweets")
//                .addPathSegment(tweeterId)
//                .addPathSegment("liking_users");
//        urlBuilder.addQueryParameter("max_results", "100");
//        if (StringUtils.hasLength(paginationToken)) {
//            urlBuilder.addQueryParameter("pagination_token", paginationToken);
//        }
//        return executeGet(client, bearer, urlBuilder.build(), LikingUserResp.class);
//    }
//
//    /**
//     * 查询单个用户
//     *
//     * @param client okhttp
//     * @param bearer bearer token
//     * @param userId user id
//     * @return RetweetUserResp
//     */
//    public static ReturnValue<SingleUserSearchResp, TwitterErrCodes> searchSingleUser(OkHttpClient client, String bearer,
//                                                                                      String userId) {
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
//        urlBuilder.addPathSegment("users")
//                .addPathSegment(userId)
//                .addQueryParameter("user.fields", "profile_image_url,verified,verified_type")
//                .addQueryParameter("tweet.fields", "geo");
//        return executeGet(client, bearer, urlBuilder.build(), SingleUserSearchResp.class);
//    }
//
//    /**
//     * 查询单条tweet的内容 (获取conversation_id)
//     *
//     * @param client    okhttp
//     * @param bearer    bearer token
//     * @param tweeterId tweeter id
//     * @return SingleTweetResp
//     */
//    public static ReturnValue<SingleTweetSearchResp, TwitterErrCodes> searchSingleTweet(OkHttpClient client, String bearer,
//                                                                                        String tweeterId) {
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
//        urlBuilder.addPathSegment("tweets")
//                .addPathSegment(tweeterId)
//                .addQueryParameter("tweet.fields", "created_at,in_reply_to_user_id,conversation_id")
//                .addQueryParameter("expansions", "in_reply_to_user_id")
//                .addQueryParameter("user.fields", "id,name");
//        return executeGet(client, bearer, urlBuilder.build(), SingleTweetSearchResp.class);
//    }
//
//    /**
//     * 搜索近期Tweet与某conversationId相关reply, 具体评论内容等
//     *
//     * @param client         okhttp
//     * @param bearer         bearer token
//     * @param conversationId conversation id
//     * @return LookupResp
//     */
//    public static ReturnValue<RecentTweetSearchResp, TwitterErrCodes> searchRecentTweet(OkHttpClient client, String bearer,
//                                                                                        String conversationId, String startTime) {
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
//        urlBuilder.addPathSegment("tweets")
//                .addPathSegment("search")
//                .addPathSegment("recent")
//                .addQueryParameter("query", "in_reply_to_tweet_id:" + conversationId);
//        if (StringUtils.hasLength(startTime)) {
//            urlBuilder.addQueryParameter("start_time", startTime);
//        }
//        urlBuilder.addQueryParameter("tweet.fields", "created_at")
//                .addQueryParameter("expansions", "author_id")
//                .addQueryParameter("max_results", "100");
//        return executeGet(client, bearer, urlBuilder.build(), RecentTweetSearchResp.class);
//    }
//
//    /**
//     * 搜索近期Tweet与某conversationId相关reply, 具体评论内容等
//     *
//     * @param client         okhttp
//     * @param bearer         bearer token
//     * @param conversationId conversation id
//     * @return LookupResp
//     */
//    public static ReturnValue<RecentTweetSearchResp, TwitterErrCodes> searchRecentTweetNext(OkHttpClient client, String bearer,
//                                                                                            String conversationId, String startTime,String nextToken) {
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
//        urlBuilder.addPathSegment("tweets")
//            .addPathSegment("search")
//            .addPathSegment("recent")
//            .addQueryParameter("query", "in_reply_to_tweet_id:" + conversationId);
//        if (StringUtils.hasLength(startTime)) {
//            urlBuilder.addQueryParameter("start_time", startTime);
//        }
//        if (StringUtils.hasLength(nextToken)) {
//            urlBuilder.addQueryParameter("pagination_token", nextToken);
//        }
//        urlBuilder.addQueryParameter("tweet.fields", "created_at")
//            .addQueryParameter("expansions", "author_id")
//            .addQueryParameter("max_results", "100");
//        return executeGet(client, bearer, urlBuilder.build(), RecentTweetSearchResp.class);
//    }
//
//    /**
//     * 搜索全部Tweet与某conversationId相关reply, 具体评论内容等
//     * tweets/search/recent 一页数据只有最大200条数据
//     * tweets/search/all    一页数据最大有500条
//     *
//     * @param client         okhttp
//     * @param bearer         bearer token
//     * @param conversationId conversation id
//     * @return LookupResp
//     */
//    public static ReturnValue<RecentTweetSearchResp, TwitterErrCodes> searchAllTweetNext(OkHttpClient client, String bearer,
//                                                                                            String conversationId, String startTime, String sinceId, String nextToken) {
//        // 原来调用的是 tweets/search/recent 区别是recent一页只有最大200条数据, all一页最大有500条
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
//        urlBuilder.addPathSegment("tweets")
//            .addPathSegment("search")
//            .addPathSegment("all")
//            .addQueryParameter("query", "in_reply_to_tweet_id:" + conversationId);
//        if (StringUtils.hasLength(startTime)) {
//            urlBuilder.addQueryParameter("start_time", startTime);
//        }
//        if (StringUtils.hasLength(sinceId)) {
//            urlBuilder.addQueryParameter("since_id", sinceId);
//        }
//        if (StringUtils.hasLength(nextToken)) {
//            urlBuilder.addQueryParameter("pagination_token", nextToken);
//        }
//        urlBuilder.addQueryParameter("tweet.fields", "created_at")
//            .addQueryParameter("expansions", "author_id")
//            .addQueryParameter("max_results", "500");
//        return executeGet(client, bearer, urlBuilder.build(), RecentTweetSearchResp.class);
//    }
//
//
//    /**
//     * Execute get request
//     */
//    private static <T> ReturnValue<T, TwitterErrCodes> executeGet(OkHttpClient client, String bearer, HttpUrl url, Class<T> respType) {
//
//        log.debug(">>> [TwitterRequestUtil] get request on url:{}", url);
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", "Bearer " + bearer)
//                .get()
//                .build();
//
//        TwitterErrCodes err = TwitterErrCodes.DEFAULT;
//        try (Response response = client.newCall(request).execute()) {
//            // a. success
//            if (response.isSuccessful() && response.body() != null) {
//                final String respStr = response.body().string();
//                log.debug("<<< [TwitterRequestUtil] response:{}", respStr);
//                return ReturnValue.withOk(
//                        JSON.parseObject(respStr, respType));
//            }
//            // b. failed
//            final int respCode = response.code();
//            log.error("<<< [TwitterRequestUtil] Request to {} code: {}, msg: {}, body: {}",
//                    url, respCode, response.message(), null == response.body() ? "" : response.body().string());
//            err = TwitterErrCodes.findByStatusCode(respCode);
//
//        } catch (IOException e) {
//            // c. ex
//            log.error("<<< [TwitterRequestUtil] IO Exception in makeRequest", e);
//        }
//        return ReturnValue.withError(err);
//    }
//
//
//}
