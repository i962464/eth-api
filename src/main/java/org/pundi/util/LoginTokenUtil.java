package org.pundi.util;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

/**
 * Login related util
 *
 * @author Roylic
 * 2024/1/26
 */
public class LoginTokenUtil {

    private static final HashFunction hashFunction = Hashing.goodFastHash(128);
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();


    /**
     * Generate Login needs token
     */
    public static String generateLoginToken(String userTwitterId) {
        long timestamp = Instant.now().getEpochSecond(); // Current time in seconds since epoch
        String randomPart = generateRandomString(16); // Generate a random string for added uniqueness
        // Serialize userId, timestamp, and randomPart
        String tokenData = userTwitterId + "|" + timestamp + "|" + randomPart;
        // Encode the token using Base64 to ensure it's URL-safe and compact
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenData.getBytes(StandardCharsets.UTF_8));

    }

    /**
     * Hash input
     */
    private static String hashInputStr(String userId) {
        String hash = hashFunction.hashString(userId, StandardCharsets.UTF_8).toString();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash.getBytes());
    }

    /**
     * Random digits
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
