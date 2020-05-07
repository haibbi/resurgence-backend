package tr.com.milia.resurgence.security;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static tr.com.milia.resurgence.security.TokenUtils.extractRefreshToken;

@RestController
@RequestMapping("/security")
public class SecurityController {

	private final TokenService tokenService;

	public SecurityController(TokenService tokenService) {
		this.tokenService = tokenService;
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

}
