package org.pundi.util.twitter;

import com.github.scribejava.core.builder.api.DefaultApi10a;

/**
 * XApi class
 *
 * @author t
 * @date 2024/6/6
 */
public class XApi extends DefaultApi10a {

	private static final String AUTHORIZE_URL = "https://api.x.com/oauth/authorize";
	private static final String REQUEST_TOKEN_RESOURCE = "api.x.com/oauth/request_token";
	private static final String ACCESS_TOKEN_RESOURCE = "api.x.com/oauth/access_token";

	protected XApi() {
	}

	public static XApi instance() {
		return InstanceHolder.INSTANCE;
	}

	public String getAccessTokenEndpoint() {
		return "https://api.x.com/oauth/access_token";
	}

	public String getRequestTokenEndpoint() {
		return "https://api.x.com/oauth/request_token";
	}

	public String getAuthorizationBaseUrl() {
		return "https://api.x.com/oauth/authorize";
	}

	private static class InstanceHolder {
		private static final XApi INSTANCE = new XApi();

		private InstanceHolder() {
		}
	}

	public static class Authenticate extends XApi {
		private static final String AUTHENTICATE_URL = "https://api.x.com/oauth/authenticate";

		private Authenticate() {
		}

		public static Authenticate instance() {
			return InstanceHolder.INSTANCE;
		}

		public String getAuthorizationBaseUrl() {
			return "https://api.x.com/oauth/authenticate";
		}

		private static class InstanceHolder {
			private static final Authenticate INSTANCE = new Authenticate();

			private InstanceHolder() {
			}
		}
	}

}
