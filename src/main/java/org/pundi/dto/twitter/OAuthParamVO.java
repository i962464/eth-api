package org.pundi.dto.twitter;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OAuth controller related params
 *
 * @author Roylic
 * 2024/1/25
 */
public class OAuthParamVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OAuthPrepareOutput {

        @ApiModelProperty(value = "OAuthToken, 需要保存到获得userId", example = "CUYVpQAAAAABr1btAAABjUQtttY", required = true)
        private String oauthToken;

        @ApiModelProperty(value = "授权跳转url", example = "https://api.twitter.com/oauth/authenticate?oauth_token=CUYVpQAAAAABr1btAAABjUQtttY", required = true)
            private String authorizationUrl;
    }

}
