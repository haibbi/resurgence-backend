package tr.com.milia.resurgence.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

	private final DecodedJWT jwt;

	public JWTAuthenticationToken(String token) {
		super(Optional.ofNullable(JWT.decode(token).getClaim("roles"))
			.map(c -> c.asList(String.class))
			.stream()
			.flatMap(Collection::stream)
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList()));
		this.jwt = JWT.decode(token);
	}

	@Override
	public Object getCredentials() {
		return jwt;
	}

	@Override
	public Object getPrincipal() {
		return jwt.getSubject();
	}

	@Override
	public boolean isAuthenticated() {
		return jwt.getExpiresAt().before(new Date());
	}

	@Override
	public String getName() {
		return jwt.getSubject();
	}

}
