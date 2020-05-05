package tr.com.milia.resurgence.i18n;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class InternationalizationExceptionAdvice {
	private final MessageSource messageSource;

	public InternationalizationExceptionAdvice(@Qualifier("defaultMessageSource") MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(LocalizedException.class)
	public ResponseEntity<LocalizedResponse> onLocalizedException(LocalizedException e, Locale locale) {
		String message = messageSource.getMessage(e, locale);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new LocalizedResponse(message, e));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void onLocalizedException() {
		// do nothing
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<LocalizedResponse> onNoSuchElementException(Locale locale) {
		String message = messageSource.getMessage(new DefaultMessageSourceResolvable("not.found"), locale);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new LocalizedResponse(message, null));
	}


}
