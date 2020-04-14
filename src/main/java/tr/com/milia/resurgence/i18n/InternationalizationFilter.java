package tr.com.milia.resurgence.i18n;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InternationalizationFilter implements WebFilter {

	private final List<Locale> supportedLocales = new ArrayList<>(4);
	private Locale defaultLocale;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		List<Locale> requestLocales = null;
		try {
			requestLocales = exchange.getRequest().getHeaders().getAcceptLanguageAsLocales();
		} catch (IllegalArgumentException ex) {
			// Invalid Accept-Language header: treat as empty for matching purposes
		}
		LocaleContextHolder.setLocaleContext(new SimpleLocaleContext(resolveSupportedLocale(requestLocales)));
		return chain.filter(exchange).doOnTerminate(LocaleContextHolder::resetLocaleContext);
	}

	public List<Locale> getSupportedLocales() {
		return this.supportedLocales;
	}

	public void setSupportedLocales(List<Locale> locales) {
		this.supportedLocales.clear();
		this.supportedLocales.addAll(locales);
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	private Locale resolveSupportedLocale(@Nullable List<Locale> requestLocales) {
		if (CollectionUtils.isEmpty(requestLocales)) {
			return this.defaultLocale;  // may be null
		}
		List<Locale> supportedLocales = getSupportedLocales();
		if (supportedLocales.isEmpty()) {
			return requestLocales.get(0);  // never null
		}

		Locale languageMatch = null;
		for (Locale locale : requestLocales) {
			if (supportedLocales.contains(locale)) {
				if (languageMatch == null || languageMatch.getLanguage().equals(locale.getLanguage())) {
					// Full match: language + country, possibly narrowed from earlier language-only match
					return locale;
				}
			} else if (languageMatch == null) {
				// Let's try to find a language-only match as a fallback
				for (Locale candidate : supportedLocales) {
					if (!StringUtils.hasLength(candidate.getCountry()) &&
						candidate.getLanguage().equals(locale.getLanguage())) {
						languageMatch = candidate;
						break;
					}
				}
			}
		}
		if (languageMatch != null) {
			return languageMatch;
		}

		return (this.defaultLocale != null ? this.defaultLocale : requestLocales.get(0));
	}
}
