package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class FamilyNotFoundException extends LocalizedException {
	public FamilyNotFoundException() {
		super("family.not.found");
	}
}
