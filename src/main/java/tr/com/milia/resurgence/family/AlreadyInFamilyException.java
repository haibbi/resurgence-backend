package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class AlreadyInFamilyException extends LocalizedException {
	public AlreadyInFamilyException() {
		super("already.in.family");
	}
}
