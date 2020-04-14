package tr.com.milia.resurgence.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenGenerator {

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		String accessJWTId = UUID.randomUUID().toString();
		String refreshJWTId = UUID.randomUUID().toString();

		JWTCreator.Builder builder = JWT.create()
			.withIssuer("milia")
			.withAudience("access")
			.withIssuedAt(Date.from(now))
			.withExpiresAt(Date.from(now.plus(1, ChronoUnit.HOURS)))
			.withSubject(authentication.getName())
			.withJWTId(accessJWTId)
			.withClaim("r-jti", refreshJWTId);

		return builder.sign(Algorithm.HMAC512("s3cr3t"));
	}

}
