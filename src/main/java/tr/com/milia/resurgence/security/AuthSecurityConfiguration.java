package tr.com.milia.resurgence.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tr.com.milia.resurgence.account.AccountService;
import tr.com.milia.resurgence.player.PlayerService;

import java.security.SecureRandom;

@Configuration
@EnableConfigurationProperties({SecurityConfigurationProperties.class})
public class AuthSecurityConfiguration {

	private final SecurityConfigurationProperties properties;

	public AuthSecurityConfiguration(SecurityConfigurationProperties properties) {
		this.properties = properties;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10, new SecureRandom());
	}

	@Bean
	UserDetailsService userDetailsService(AccountService accountService, PlayerService playerService) {
		return new AccountDetailsService(accountService, playerService);
	}

	@Bean
	TokenService tokenService(UserDetailsService userDetailsService) {
		return new TokenService(properties, userDetailsService);
	}

}
