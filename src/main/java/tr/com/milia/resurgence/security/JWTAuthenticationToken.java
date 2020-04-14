package tr.com.milia.resurgence.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

	private final String principal;
	private final DecodedJWT credentials;

	public JWTAuthenticationToken(String token) {
		super(Optional.ofNullable(JWT.decode(token).getClaim("roles"))
			.map(c -> c.asList(String.class))
			.stream()
			.flatMap(Collection::stream)
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList()));

		DecodedJWT jwt = JWT.decode(token);
		principal = jwt.getSubject();
		credentials = jwt;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}


	@Override
	public boolean isAuthenticated() {
		// todo consider
		return credentials != null;
	}
}
