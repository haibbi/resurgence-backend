package tr.com.milia.resurgence.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static tr.com.milia.resurgence.security.Claims.*;

public class TokenService {

	private final SecurityConfigurationProperties properties;
	private final UserDetailsService userDetailsService;
	private final JWTVerifier accessTokenVerifier;
	private final JWTVerifier refreshTokenVerifier;

	public TokenService(SecurityConfigurationProperties properties,
						UserDetailsService userDetailsService) {
		this.properties = properties;
		this.userDetailsService = userDetailsService;

		accessTokenVerifier = JWT.require(Algorithm.HMAC512(properties.getSecret()))
			.withIssuer(ISSUER)
			.withAudience(ACCESS_AUDIENCE)
			.build();

		refreshTokenVerifier = JWT.require(Algorithm.HMAC512(properties.getSecret()))
			.withIssuer(ISSUER)
			.withAudience(REFRESH_AUDIENCE)
			.build();
	}

	public TokenResponse generateToken(Authentication authentication) {
		return generateTokenInternal(authentication);
	}

	public TokenResponse refreshToken(String refreshToken) {
		DecodedJWT decodedJWT = verifyRefreshToken(refreshToken);

		String subject = decodedJWT.getSubject();

		UserDetails user = userDetailsService.loadUserByUsername(subject);

		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				user, user.getPassword(), user.getAuthorities()
			);

		return generateTokenInternal(authentication);
	}

	private TokenResponse generateTokenInternal(Authentication authentication) {
		Instant now = Instant.now();

		String accessTokenId = UUID.randomUUID().toString();
		String refreshTokenId = UUID.randomUUID().toString();

		String accessToken = generateAccessToken(now, authentication, accessTokenId, refreshTokenId);
		String refreshToken = generateRefreshToken(now, authentication, refreshTokenId);

		return new TokenResponse(accessToken, refreshToken);
	}

	public DecodedJWT verify(String token) {
		return accessTokenVerifier.verify(token);
	}

	private String generateAccessToken(Instant now, Authentication authentication, String jwtId, String rjwtId) {
		String[] authorities = authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.toArray(String[]::new);

		String subject = authentication.getName();

		JWTCreator.Builder builder = JWT.create()
			.withIssuer(ISSUER)
			.withAudience(ACCESS_AUDIENCE)
			.withIssuedAt(Date.from(now))
			.withExpiresAt(Date.from(Instant.now().plus(properties.getAccessTokenExpiration())))
			.withSubject(subject)
			.withJWTId(jwtId)
			.withClaim(REFRESH_TOKEN_ID, rjwtId)
			.withArrayClaim(ROLE, authorities);

		if (authentication.getPrincipal() instanceof ResurgenceUser) {
			ResurgenceUser principal = (ResurgenceUser) authentication.getPrincipal();
			builder.withClaim(PLAYER, principal.getPlayerName());
		}

		return builder.sign(Algorithm.HMAC512(properties.getSecret()));
	}

	private String generateRefreshToken(Instant now, Authentication authentication, String jwtId) {
		String subject = authentication.getName();

		JWTCreator.Builder builder = JWT.create()
			.withIssuer(ISSUER)
			.withAudience(REFRESH_AUDIENCE)
			.withIssuedAt(Date.from(now))
			.withExpiresAt(Date.from(Instant.now().plus(properties.getRefreshTokenExpiration())))
			.withJWTId(jwtId)
			.withSubject(subject);

		return builder.sign(Algorithm.HMAC512(properties.getSecret()));
	}

	private DecodedJWT verifyRefreshToken(String token) {
		return refreshTokenVerifier.verify(token);
	}

}
