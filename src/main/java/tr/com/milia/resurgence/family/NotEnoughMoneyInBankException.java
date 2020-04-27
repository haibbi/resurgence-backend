package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class NotEnoughMoneyInBankException extends LocalizedException {
	public NotEnoughMoneyInBankException() {
		super("not.enough.money.in.bank");
	}
}
