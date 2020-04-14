package tr.com.milia.resurgence.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;
import tr.com.milia.resurgence.account.AccountService;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

	private final TokenGenerator tokenGenerator;
	private final AccountService accountService;

	public SecurityConfiguration(TokenGenerator tokenGenerator, AccountService accountService) {
		this.tokenGenerator = tokenGenerator;
		this.accountService = accountService;
	}

	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
		return http
			.exceptionHandling()
			.authenticationEntryPoint(new CustomServerAuthenticationEntryPoint())
			.and()
			.authorizeExchange()
			.pathMatchers("/", "/login").permitAll() // todo consider
			.anyExchange().authenticated()
			.and()
			.addFilterAt(authenticationFilter(), SecurityWebFiltersOrder.FORM_LOGIN)
			.addFilterAt(bearerFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
			.build();
	}

	@Bean
	public ReactiveUserDetailsService userDetailsService() {
		return new AccountReactiveUserDetailsService(accountService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// todo configure
		return new BCryptPasswordEncoder();
	}

	private AuthenticationWebFilter authenticationFilter() {
		var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
		var authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);

		authenticationManager.setPasswordEncoder(passwordEncoder());
		authenticationWebFilter.setServerAuthenticationConverter(new UsernamePasswordAuthenticationConverter());
		authenticationWebFilter.setAuthenticationSuccessHandler(
			new UsernamePasswordAuthenticationSuccessHandler(tokenGenerator));

		return authenticationWebFilter;
	}

	private AuthenticationWebFilter bearerFilter() {
		var authenticationWebFilter = new AuthenticationWebFilter((ReactiveAuthenticationManager) Mono::just);
		authenticationWebFilter.setServerAuthenticationConverter(new TokenAuthenticationConverter());
		return authenticationWebFilter;
	}

}
