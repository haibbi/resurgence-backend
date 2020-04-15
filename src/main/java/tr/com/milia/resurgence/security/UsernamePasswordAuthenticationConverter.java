package tr.com.milia.resurgence.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

public class UsernamePasswordAuthenticationConverter implements ServerAuthenticationConverter {

	private final PathPattern pathPattern = new PathPatternParser().parse("/login");

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		var request = exchange.getRequest();
		if (!HttpMethod.POST.equals(request.getMethod())) return Mono.empty();
		if (!pathPattern.matches(exchange.getRequest().getPath().pathWithinApplication())) return Mono.empty();

		return exchange.getFormData().map(this::createAuthentication);
	}

	private UsernamePasswordAuthenticationToken createAuthentication(
		MultiValueMap<String, String> data) {
		String username = data.getFirst("username");
		String password = data.getFirst("password");
		return new UsernamePasswordAuthenticationToken(username, password);
	}
}
