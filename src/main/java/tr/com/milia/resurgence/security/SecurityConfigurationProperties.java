package tr.com.milia.resurgence.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("resurgence.security")
public class SecurityConfigurationProperties {

	private Duration accessTokenExpiration;

	private Duration refreshTokenExpiration;

	private String secret;

	public Duration getAccessTokenExpiration() {
		return accessTokenExpiration;
	}

	public void setAccessTokenExpiration(Duration accessTokenExpiration) {
		this.accessTokenExpiration = accessTokenExpiration;
	}

	public Duration getRefreshTokenExpiration() {
		return refreshTokenExpiration;
	}

	public void setRefreshTokenExpiration(Duration refreshTokenExpiration) {
		this.refreshTokenExpiration = refreshTokenExpiration;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
