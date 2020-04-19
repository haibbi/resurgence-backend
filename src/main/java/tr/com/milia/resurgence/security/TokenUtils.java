package tr.com.milia.resurgence.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class TokenUtils {

	/**
	 * Accept access token from http parameter
	 */
	public static final String HTTP_PARAMETER = "access_token";

	/**
	 * Token type-prefix...
	 */
	public static final String TOKEN_TYPE = "Bearer";

	public static DecodedJWT decode(String token) {
		return JWT.decode(token);
	}

	public static Optional<String> extractAccessToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader != null && !authorizationHeader.isBlank()) {
			return Optional.of(authorizationHeader.substring(TOKEN_TYPE.length()).trim());
		}

		String accessTokenParameter = request.getParameter(HTTP_PARAMETER);

		return Optional.ofNullable(accessTokenParameter);
	}
}
