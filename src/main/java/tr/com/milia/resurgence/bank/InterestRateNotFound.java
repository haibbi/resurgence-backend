package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class InterestRateNotFound extends LocalizedException {
	public InterestRateNotFound() {
		super("interest.rate.not.found");
	}
}
