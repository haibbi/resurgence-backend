package tr.com.milia.resurgence.account;

import tr.com.milia.resurgence.i18n.LocalizedException;

class EmailAlreadyExistException extends LocalizedException {
	public EmailAlreadyExistException(String email) {
		super("email.address.already.exists", email);
	}
}
