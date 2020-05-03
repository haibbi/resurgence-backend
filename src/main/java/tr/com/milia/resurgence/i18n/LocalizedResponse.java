package tr.com.milia.resurgence.i18n;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class LocalizedResponse {
	private final String message;

	@JsonIgnoreProperties({"stack_trace", "codes", "suppressed"})
	private final LocalizedException detail;

	public LocalizedResponse(String message, LocalizedException detail) {
		this.message = message;
		this.detail = detail;
	}

	public String getMessage() {
		return message;
	}

	public LocalizedException getDetail() {
		return detail;
	}
}
