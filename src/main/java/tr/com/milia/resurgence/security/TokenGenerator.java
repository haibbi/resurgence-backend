package tr.com.milia.resurgence.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenGenerator {

	public Token generateToken(Authentication authentication) {
		var now = Instant.now();
		var accessJWTId = UUID.randomUUID().toString();
		var refreshJWTId = UUID.randomUUID().toString();

		var accessTokenBuilder = JWT.create()
			.withIssuer("milia")
			.withAudience("access")
			.withIssuedAt(Date.from(now))
			.withExpiresAt(Date.from(now.plus(1, ChronoUnit.HOURS)))
			.withSubject(authentication.getName())
			.withJWTId(accessJWTId)
			.withClaim("r-jti", refreshJWTId);

		var refreshTokenBuilder = JWT.create()
			.withIssuer("milia")
			.withAudience("refresh")
			.withIssuedAt(Date.from(now))
			.withExpiresAt(Date.from(now.plus(1, ChronoUnit.DAYS)))
			.withSubject(authentication.getName())
			.withJWTId(refreshJWTId);

		return new Token(accessTokenBuilder.sign(Algorithm.HMAC512("s3cr3t")),
			refreshTokenBuilder.sign(Algorithm.HMAC512("s3cr3t")));
	}

}
