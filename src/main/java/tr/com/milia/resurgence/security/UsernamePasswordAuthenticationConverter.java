package tr.com.milia.resurgence.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

public class UsernamePasswordAuthenticationConverter implements ServerAuthenticationConverter {
	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		var pathPattern = new PathPatternParser().parse("/login");

		if (pathPattern.matches(exchange.getRequest().getPath().pathWithinApplication())) {
			var queryParams = exchange.getRequest().getQueryParams();
			var username = queryParams.getFirst("username");
			var password = queryParams.getFirst("password");

			if (username == null || password == null) return Mono.empty();
			return Mono.just(new UsernamePasswordAuthenticationToken(username, password));
		}
		return Mono.empty();
	}
}
