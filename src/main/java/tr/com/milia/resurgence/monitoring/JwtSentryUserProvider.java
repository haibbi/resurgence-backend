package tr.com.milia.resurgence.monitoring;

import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.sentry.protocol.User;
import io.sentry.spring.SentryUserProvider;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tr.com.milia.resurgence.security.Claims;
import tr.com.milia.resurgence.security.TokenAuthentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

public final class JwtSentryUserProvider implements SentryUserProvider {

	private static String toIpAddress(final HttpServletRequest request) {
		final String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress != null) {
			return ipAddress.contains(",") ? ipAddress.split(",")[0].trim() : ipAddress;
		} else {
			return request.getRemoteAddr();
		}
	}

	@Override
	public User provideUser() {
		final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			final ServletRequestAttributes servletRequestAttributes =
				(ServletRequestAttributes) requestAttributes;
			final HttpServletRequest request = servletRequestAttributes.getRequest();

			final User user = new User();
			user.setIpAddress(toIpAddress(request));
			Principal principal = request.getUserPrincipal();
			if (principal != null) {
				user.setUsername(principal.getName());

				if (principal instanceof TokenAuthentication) {
					TokenAuthentication authentication = (TokenAuthentication) principal;
					user.setId(authentication.getName());
					user.setUsername(authentication.getPlayerName());
					user.setEmail(authentication.getName());

					DecodedJWT jwt = authentication.getCredentials();
					user.setOthers(Map.of(
						PublicClaims.JWT_ID, jwt.getId(),
						Claims.REFRESH_TOKEN_ID, jwt.getClaim(Claims.REFRESH_TOKEN_ID).asString(),
						Claims.ROLE, Arrays.toString(jwt.getClaim(Claims.ROLE).asArray(String.class))
					));
				}
			}
			return user;
		}
		return null;
	}
}
