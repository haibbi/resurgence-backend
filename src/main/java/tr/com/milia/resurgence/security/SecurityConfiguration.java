package tr.com.milia.resurgence.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
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
			.csrf().disable() // todo enable
			.authorizeExchange()
			.pathMatchers("/", "/account").permitAll()
			.anyExchange().authenticated()
			.and()
			.httpBasic().disable()
			.addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
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

	private AuthenticationWebFilter basicAuthenticationFilter() {
		final UserDetailsRepositoryReactiveAuthenticationManager authManager;
		final AuthenticationWebFilter basicAuthenticationFilter;
		final ServerAuthenticationSuccessHandler successHandler;

		authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
		authManager.setPasswordEncoder(passwordEncoder()); // todo bir tane üret kardeşim
		successHandler = serverAuthenticationSuccessHandler();

		basicAuthenticationFilter = new AuthenticationWebFilter(authManager);
		basicAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);

		return basicAuthenticationFilter;

	}

	private AuthenticationWebFilter bearerAuthenticationFilter() {
		var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());

		var authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
		authenticationWebFilter.setServerAuthenticationConverter(exchange -> {
			var queryParams = exchange.getRequest().getQueryParams();
			var username = queryParams.getFirst("username");
			var password = queryParams.getFirst("password");

			if (username == null || password == null) return Mono.empty();

			return Mono.just(new UsernamePasswordAuthenticationToken(username, password));
		});
		authenticationWebFilter.setAuthenticationSuccessHandler(serverAuthenticationSuccessHandler());

		return authenticationWebFilter;
	}

	private ServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler() {
		return (webFilterExchange, authentication) -> {
			String token = tokenGenerator.generateToken(authentication);

			ServerWebExchange exchange = webFilterExchange.getExchange();
			exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION,
				String.join(" ", "Bearer", token));
			return webFilterExchange.getChain().filter(exchange);
		};
	}

}
