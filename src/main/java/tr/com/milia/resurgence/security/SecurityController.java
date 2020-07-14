package tr.com.milia.resurgence.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.account.AccountService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

import static tr.com.milia.resurgence.security.TokenUtils.extractRefreshToken;

@RestController
@RequestMapping("/security")
public class SecurityController {

	private final TokenService tokenService;
	private final UserDetailsService userDetailsService;
	private final AccountService accountService;
	private final Map<String, OAuth2TokenVerifier> tokenVerifiers;

	public SecurityController(
		TokenService tokenService,
		UserDetailsService userDetailsService,
		AccountService accountService,
		Map<String, OAuth2TokenVerifier> tokenVerifiers
	) {
		this.tokenService = tokenService;
		this.userDetailsService = userDetailsService;
		this.accountService = accountService;
		this.tokenVerifiers = tokenVerifiers;
	}

	@PostMapping("/refresh")
	public TokenResponse refresh(HttpServletRequest request) {
		String refreshToken = extractRefreshToken(request).orElseThrow(() -> {
			throw new PreAuthenticatedCredentialsNotFoundException("Missing refresh token");
		});

		return tokenService.refreshToken(refreshToken);
	}

	@GetMapping("/oauth2-token")
	public Map<String, String> oauth2Token(
		@RequestParam("access_token") String accessToken,
		@RequestParam("refresh_token") String refreshToken
	) {
		return Map.of("access_token", accessToken, "refresh_token", refreshToken);
	}

	@PostMapping("/oauth2/{provider}")
	public ResponseEntity<TokenResponse> login(
		@PathVariable("provider") String provider,
		@RequestBody OAuth2Request oauth2Request
	) {
		Optional<OAuth2TokenVerifier> tokenVerifier = getProviderVerifier(provider);

		if (tokenVerifier.isEmpty()) return ResponseEntity.notFound().build();

		OAuth2User oAuth2User = tokenVerifier.get().verify(oauth2Request);

		String username = oAuth2User.getName();

		accountService.oauth2Register(username);
		UserDetails user = userDetailsService.loadUserByUsername(username);

		UsernamePasswordAuthenticationToken usernamePasswordAuth = new UsernamePasswordAuthenticationToken(
			user, user.getPassword(), user.getAuthorities());

		TokenResponse token = tokenService.generateToken(usernamePasswordAuth);
		return ResponseEntity.ok(token);
	}

	private Optional<OAuth2TokenVerifier> getProviderVerifier(String provider) {
		return tokenVerifiers.values().stream()
			.filter(tv -> tv.name().equals(provider))
			.findFirst();
	}

}
