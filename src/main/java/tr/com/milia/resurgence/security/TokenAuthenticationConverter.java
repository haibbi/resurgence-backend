package tr.com.milia.resurgence.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TokenAuthenticationConverter implements ServerAuthenticationConverter {
	private static final String BEARER_TOKEN_PREFIX = "Bearer ";
	private final JWTVerifier verifier;

	public TokenAuthenticationConverter() {
		this.verifier = JWT.require(Algorithm.HMAC512("s3cr3t"))
			.withIssuer("milia")
			.withAudience("access")
			.build();
	}

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) {
			return Mono.empty();
		}

		var accessToken = authorizationHeader.substring(BEARER_TOKEN_PREFIX.length());
		verifier.verify(accessToken);

		return Mono.just(new JWTAuthenticationToken(JWT.decode(accessToken)));
	}
}
