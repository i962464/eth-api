package org.pundi.dto.twitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OAuth 1.0a Step 2
 *
 * @author Roylic
 * 2024/1/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthGrantResp {
    private String userToken;
    private String userTokenSecret;
    private long expiryTime;
    private String refreshToken;
    private String profileImageUrl;
    private String userId;
    private String userName;
}
