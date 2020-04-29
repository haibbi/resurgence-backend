package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class FamilyAccessDeniedException extends LocalizedException {
	public FamilyAccessDeniedException() {
		super("family.access.denied");
	}
}
