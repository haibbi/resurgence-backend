package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class MemberOutOfBoundsException extends LocalizedException {
	public MemberOutOfBoundsException() {
		super("member.out.of.bounds");
	}
}
