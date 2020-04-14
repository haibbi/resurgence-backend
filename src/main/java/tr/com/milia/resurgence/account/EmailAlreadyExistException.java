package tr.com.milia.resurgence.account;

import tr.com.milia.resurgence.i18n.LocalizedException;

class EmailAlreadyExistException extends LocalizedException {
	public EmailAlreadyExistException(String email) {
		super(new String[]{"email.address.already.exists"}, new String[]{email});
	}
}
