package tr.com.milia.resurgence.security;

import java.io.Serializable;

public class SuccessfulAuthenticationResponse implements Serializable {
	private final String accessToken;
	private final String refreshToken;

	public SuccessfulAuthenticationResponse(String accessToken, String refreshToken) {
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
