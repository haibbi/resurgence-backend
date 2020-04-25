package tr.com.milia.resurgence.smuggling;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class NotEnoughMoneyException extends LocalizedException {
	public NotEnoughMoneyException() {
		super("not.enough.money");
	}
}
