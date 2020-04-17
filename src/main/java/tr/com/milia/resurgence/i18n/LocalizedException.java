package tr.com.milia.resurgence.i18n;

import org.springframework.context.MessageSourceResolvable;

public class LocalizedException extends RuntimeException implements MessageSourceResolvable {

	private final String[] codes;
	private final Object[] arguments;
	private final String defaultMessage;

	public LocalizedException(String code) {
		this(new String[]{code}, null, null, null);
	}

	public LocalizedException(String code, Throwable cause) {
		this(new String[]{code}, null, null, cause);
	}

	public LocalizedException(String[] codes, Object[] arguments) {
		this(codes, arguments, null, null);
	}

	public LocalizedException(String code, Object argument) {
		this(new String[]{code}, new Object[]{argument}, null, null);
	}

	public LocalizedException(String[] codes, Object[] arguments, Throwable cause) {
		this(codes, arguments, null, cause);
	}

	public LocalizedException(String[] codes, String defaultMessage, Throwable cause) {
		this(codes, null, defaultMessage, cause);
	}

	public LocalizedException(String[] codes, String defaultMessage) {
		this(codes, null, defaultMessage, null);
	}

	public LocalizedException(String[] codes, Object[] arguments, String defaultMessage, Throwable cause) {
		super(defaultMessage, cause);
		this.codes = codes;
		this.arguments = arguments;
		this.defaultMessage = defaultMessage;
	}

	@Override
	public String[] getCodes() {
		return codes;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public String getDefaultMessage() {
		return defaultMessage;
	}
}
