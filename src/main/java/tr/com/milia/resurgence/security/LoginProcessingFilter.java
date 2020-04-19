package tr.com.milia.resurgence.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger log = LoggerFactory.getLogger(LoginProcessingFilter.class);

	private static final String url = "/login";
	private final ObjectMapper mapper;
	private final TokenService tokenService;

	public LoginProcessingFilter(AuthenticationManager authManager, ObjectMapper mapper, TokenService tokenService) {
		super(new AntPathRequestMatcher(url, "POST"));
		this.mapper = mapper;
		this.tokenService = tokenService;
		setAuthenticationManager(authManager);
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

	private void writeResponseBody(HttpServletResponse response, TokenResponse token) {
		try {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(mapper.writeValueAsString(token));
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
