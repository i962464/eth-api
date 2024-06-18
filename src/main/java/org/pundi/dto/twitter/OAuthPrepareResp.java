package org.pundi.dto.twitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OAuth 1.0a Step 1
 *
 * @author Roylic
 * 2024/1/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthPrepareResp {
    private String oauthToken;
    private String oauthTokenSecret;
    private String oauthAuthenticateUrl;
}
