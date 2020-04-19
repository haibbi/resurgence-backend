package tr.com.milia.resurgence.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final TokenService tokenService;
	private final ObjectMapper mapper;

	public WebSecurityConfiguration(TokenService tokenService, ObjectMapper mapper) {
		this.tokenService = tokenService;
		this.mapper = mapper;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		var tokenFilter = new JwtTokenAuthenticationFilter(tokenService);
		var loginProcessingFilter = new LoginProcessingFilter(authenticationManager(), mapper, tokenService);

		http.csrf().disable()
			.logout().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.anonymous().and()
			.exceptionHandling().authenticationEntryPoint((req, rsp, e) ->
			rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
			.addFilterAfter(loginProcessingFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests().antMatchers("/login").permitAll().and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/account").permitAll().and()
			.authorizeRequests().anyRequest().authenticated();
	}
}
