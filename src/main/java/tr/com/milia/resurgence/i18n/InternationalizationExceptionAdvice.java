package tr.com.milia.resurgence.i18n;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InternationalizationExceptionAdvice {
	private final MessageSource messageSource;

	public InternationalizationExceptionAdvice(@Qualifier("defaultMessageSource") MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(LocalizedException.class)
	public ResponseEntity<LocalizedResponse> onLocalizedException(LocalizedException e) {
		String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new LocalizedResponse(message));
	}


}
