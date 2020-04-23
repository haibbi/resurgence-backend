package tr.com.milia.resurgence.security;

public class TokenResponse {

	private final String accessToken;
	private final String refreshToken;

	TokenResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}
