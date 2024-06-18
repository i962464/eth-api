package org.pundi.util.twitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pundi.dto.twitter.OAuthGrantResp;
import org.pundi.dto.twitter.OAuthPrepareResp;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年06月17日 16:44:00
 */
class TwitterOAuthUtilTest {

	private static final String CONSUMER_K = "j27ri5UAgjlIgT4gMZSAp1BbR";

	private static final String CONSUMER_S = "yWfw26LYObknFBOmYkeHCSzmLWo2bGk6dIInvTi0ewppV7H8rR";

	private static final ObjectMapper om = new ObjectMapper();


	/**
	 * Whole oath 1.0a process
	 */
	public static void main(String[] args) throws JsonProcessingException {

		final Scanner in = new Scanner(System.in);
		final OAuthPrepareResp oAuthPrepareResp = TwitterOAuthUtil.prepareOauthToken(
				CONSUMER_K, CONSUMER_S, "http://127.0.0.1/xplus/login")
			.getData();
		System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(oAuthPrepareResp));

		System.out.println("Request Token Url:\n" + oAuthPrepareResp.getOauthAuthenticateUrl());
		System.out.println("Add oauth_verifier below");
		System.out.print(">>>");

		final String oauthVerifier = in.nextLine();
		final OAuthGrantResp oAuthGrantResp = TwitterOAuthUtil.grantOauthAccess(CONSUMER_K, CONSUMER_S,
				oAuthPrepareResp.getOauthToken(), oAuthPrepareResp.getOauthTokenSecret(), oauthVerifier)
			.getData();
		System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(oAuthGrantResp));
	}
}