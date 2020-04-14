package tr.com.milia.resurgence.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TokenAuthenticationConverter implements ServerAuthenticationConverter {
	private static final String BEARER_TOKEN = "Bearer ";

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		String authorizationToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (authorizationToken == null || !authorizationToken.startsWith(BEARER_TOKEN)) {
			return Mono.empty();
		}

		return Mono.just(new JWTAuthenticationToken(authorizationToken.substring(BEARER_TOKEN.length())));
	}
}
