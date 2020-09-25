package tr.com.milia.resurgence.monitoring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentryConfiguration {

	@Bean
	JwtSentryUserProvider jwtSentryUserProvider() {
		return new JwtSentryUserProvider();
	}

}