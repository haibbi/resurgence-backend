package tr.com.milia.resurgence.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tr.com.milia.resurgence.i18n.LocalizedResponse;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger log = LoggerFactory.getLogger(LoginProcessingFilter.class);

	private static final String url = "/login";
	private final ObjectMapper mapper;
	private final TokenService tokenService;
	private final MessageSource messageSource;

	public LoginProcessingFilter(AuthenticationManager authManager,
								 ObjectMapper mapper,
								 TokenService tokenService,
								 MessageSource messageSource) {
		super(new AntPathRequestMatcher(url, "POST"));
		this.mapper = mapper;
		this.tokenService = tokenService;
		this.messageSource = messageSource;
		setAuthenticationManager(authManager);
		setAuthenticationFailureHandler(this::onFail);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws IOException {
		User user = mapper.readValue(request.getInputStream(), User.class);

		return getAuthenticationManager().authenticate(
			new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain chain,
											Authentication authResult) {
		TokenResponse token = tokenService.generateToken(authResult);
		writeResponseBody(response, token);
	}

	private void onFail(HttpServletRequest request,
						HttpServletResponse response,
						AuthenticationException exception) throws IOException {
		final String code;

		if (exception instanceof BadCredentialsException) {
			code = "login.bad.credentials";
		} else if (exception instanceof DisabledException) {
			code = "login.account.disabled";
		} else {
			code = "login.fail";
		}

		final String message = messageSource.getMessage(code, null, request.getLocale());
		LocalizedResponse responseBody = new LocalizedResponse(message);
		writeResponseBody(response, responseBody);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}

	private void writeResponseBody(HttpServletResponse response, Object body) {
		try {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(mapper.writeValueAsString(body));
		} catch (IOException e) {
			log.error("An error occurred while writing response body", e);
		}
	}

	private static final class User {
		private String username;
		private String password;

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}
}
