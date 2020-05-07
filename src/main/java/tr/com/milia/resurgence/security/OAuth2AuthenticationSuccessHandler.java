package tr.com.milia.resurgence.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import tr.com.milia.resurgence.account.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final AccountService accountService;
	private final TokenService tokenService;
	private final UserDetailsService userDetailsService;

	public OAuth2AuthenticationSuccessHandler(AccountService accountService,
											  TokenService tokenService,
											  UserDetailsService userDetailsService) {
		this.accountService = accountService;
		this.tokenService = tokenService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication) throws IOException {
		if (!(authentication instanceof OAuth2AuthenticationToken)) return;

		DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
		String email = (String) oAuth2User.getAttributes().get("email");
		accountService.oauth2Register(email);

		UserDetails user = userDetailsService.loadUserByUsername(email);

		UsernamePasswordAuthenticationToken usernamePasswordAuth = new UsernamePasswordAuthenticationToken(
			user, user.getPassword(), user.getAuthorities());

		TokenResponse token = tokenService.generateToken(usernamePasswordAuth);

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder
			.path("/security/oauth2-token")
			.queryParam("access_token", token.getAccessToken())
			.queryParam("refresh_token", token.getRefreshToken());
		String location = builder.build().toString();

		response.sendRedirect(location);

		clearAuthenticationAttributes(request);
	}

}
