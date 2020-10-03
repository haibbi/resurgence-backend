package tr.com.milia.resurgence.monitoring;

import io.sentry.spring.boot.SentryLogbackAppenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Configuration
public class SentryConfiguration {

	@Bean
	JwtSentryUserProvider jwtSentryUserProvider() {
		return new JwtSentryUserProvider();
	}

	@Bean
	@Profile("prod")
	@Lazy(value = false)
	SentryLogbackAppenderAutoConfiguration sentryLogbackAppenderAutoConfiguration() {
		return new SentryLogbackAppenderAutoConfiguration();
	}

}
