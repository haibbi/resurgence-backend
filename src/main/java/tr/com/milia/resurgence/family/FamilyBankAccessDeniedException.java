package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class FamilyBankAccessDeniedException extends LocalizedException {
	public FamilyBankAccessDeniedException() {
		super("family.bank.access.denied");
	}
}
