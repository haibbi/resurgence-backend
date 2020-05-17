package tr.com.milia.resurgence.i18n;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

	private final MessageSource messageSource;

	public MethodArgumentNotValidExceptionHandler(@Qualifier("defaultMessageSource") MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ResponseBody
	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public LocalizedResponse on(MethodArgumentNotValidException ex, Locale locale) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();

		StringJoiner sj = new StringJoiner("\n");
		for (FieldError fieldError : fieldErrors) {
			String message = String.join(": ",
				messageSource.getMessage(fieldError.getField(), null, fieldError.getField(), locale),
				messageSource.getMessage(fieldError, locale));
			sj.add(message);
		}

		return new LocalizedResponse(sj.toString());
	}

}
