package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class BankAccountNotFoundException extends LocalizedException {
	public BankAccountNotFoundException() {
		super("bank.account.not.found");
	}
}
