package tr.com.milia.resurgence.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static tr.com.milia.resurgence.security.TokenUtils.extractAccessToken;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);

	private final TokenService tokenService;

	public JwtTokenAuthenticationFilter(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
		throws ServletException, IOException {

		try {
			Optional<String> tokenOptional = extractAccessToken(request);

			if (tokenOptional.isEmpty()) {
				filterChain.doFilter(request, response);
				return;
			}
			String token = tokenOptional.get();
			DecodedJWT decodedJWT = tokenService.verify(token);
			TokenAuthentication tokenAuthentication = new TokenAuthentication(decodedJWT);
			SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
		} catch (JWTVerificationException e) {
			SecurityContextHolder.clearContext();
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			log.debug("JWT Verification Exception occur", e);
			return;
		} catch (Exception e) {
			log.error("An error occurred while token authentication", e);
			SecurityContextHolder.clearContext();
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}

		filterChain.doFilter(request, response);
		SecurityContextHolder.clearContext(); // todo check
	}
}
