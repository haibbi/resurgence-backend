package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class SelfInvitationException extends LocalizedException {
	public SelfInvitationException() {
		super("can.not.invite.yourself");
	}
}
