package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class InvitationNotException extends LocalizedException {
	public InvitationNotException() {
		super("invitation.not.found");
	}
}
