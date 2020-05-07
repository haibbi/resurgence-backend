package tr.com.milia.resurgence.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final TokenService tokenService;
	private final ObjectMapper mapper;
	private final OAuth2AuthenticationSuccessHandler oAuth2ASH;

	public WebSecurityConfiguration(TokenService tokenService,
									ObjectMapper mapper,
									OAuth2AuthenticationSuccessHandler oAuth2ASH) {
		this.tokenService = tokenService;
		this.mapper = mapper;
		this.oAuth2ASH = oAuth2ASH;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		var tokenFilter = new JwtTokenAuthenticationFilter(tokenService);
		var loginProcessingFilter = new LoginProcessingFilter(authenticationManager(), mapper, tokenService);
		var characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);

		http.csrf().disable()
			.logout().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.anonymous().and()
			.exceptionHandling().authenticationEntryPoint((req, rsp, e) ->
			rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
			.oauth2Login().successHandler(oAuth2ASH).and()
			.addFilterAfter(loginProcessingFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(characterEncodingFilter, LoginProcessingFilter.class)
			.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll().and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/account").permitAll().and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/security/refresh").permitAll().and()
			.authorizeRequests().anyRequest().authenticated();
	}
}
