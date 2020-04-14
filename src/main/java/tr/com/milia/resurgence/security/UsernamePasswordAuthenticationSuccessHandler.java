package tr.com.milia.resurgence.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class UsernamePasswordAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

	private final TokenGenerator tokenGenerator;

	public UsernamePasswordAuthenticationSuccessHandler(TokenGenerator tokenGenerator) {
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
		String token = tokenGenerator.generateToken(authentication);

		ServerWebExchange exchange = webFilterExchange.getExchange();
		exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION,
			String.join(" ", "Bearer", token));
		return webFilterExchange.getChain().filter(exchange);
	}
}
