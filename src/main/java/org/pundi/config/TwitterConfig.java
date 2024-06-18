package org.pundi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Twitter related conf.
 *
 * @author Roylic
 * 2024/1/26
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "twitter.conf")
public class TwitterConfig {
    private String consumerKey;
    private String consumerSecret;
    private String bearerToken;
    private String callbackUrl;
    private String scope;
    private String clientId;
    private String clientSecret;
    private Integer taskPostConsumeLimit = 5;
    private Integer taskTweetConsumeLimit = 5;
}
