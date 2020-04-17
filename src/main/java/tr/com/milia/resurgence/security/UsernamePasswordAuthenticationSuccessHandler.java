package tr.com.milia.resurgence.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

public class UsernamePasswordAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

	private final TokenGenerator tokenGenerator;
	private final ObjectMapper objectMapper;

	public UsernamePasswordAuthenticationSuccessHandler(TokenGenerator tokenGenerator, ObjectMapper objectMapper) {
		this.tokenGenerator = tokenGenerator;
		this.objectMapper = objectMapper;
	}

	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
		var token = tokenGenerator.generateToken(authentication);
		var response = new SuccessfulAuthenticationResponse(token.accessToken, token.refreshToken);

		return writeResponseBody(webFilterExchange, response);
	}

	private Mono<Void> writeResponseBody(WebFilterExchange webFilterExchange,
										 SuccessfulAuthenticationResponse response) {
		var exchange = webFilterExchange.getExchange();
		exchange.getResponse().setStatusCode(HttpStatus.OK);
		exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		var map = Mono.<byte[]>create(sink -> {
			try {
				sink.success(objectMapper.writeValueAsBytes(response));
			} catch (JsonProcessingException e) {
				sink.error(e);
			}
		}).map(bytes -> exchange.getResponse().bufferFactory().wrap(bytes));

		return exchange.getResponse().writeWith(map);
	}
}
