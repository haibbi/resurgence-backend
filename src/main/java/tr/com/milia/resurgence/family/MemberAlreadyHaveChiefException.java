package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class MemberAlreadyHaveChiefException extends LocalizedException {
	public MemberAlreadyHaveChiefException() {
		super("member.already.have.chief");
	}
}
