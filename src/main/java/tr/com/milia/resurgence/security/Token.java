package tr.com.milia.resurgence.security;

class Token {
	final String accessToken;
	final String refreshToken;

	Token(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

}
