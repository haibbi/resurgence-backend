package tr.com.milia.resurgence.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.server.WebFilter;

@Configuration
public class InternationalizationConfiguration {
	@Bean
	MessageSource defaultMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasename("localization/messages");

		return messageSource;
	}

	@Bean
	WebFilter internationalizationFilter() {
		return new InternationalizationFilter();
	}
}
