package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class MemberAlreadyChiefException extends LocalizedException {
	public MemberAlreadyChiefException() {
		super("member.already.chief");
	}
}
