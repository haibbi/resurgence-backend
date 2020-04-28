package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class DuplicateFamilyNameException extends LocalizedException {
	public DuplicateFamilyNameException() {
		super("duplicate.family.name");
	}
}
