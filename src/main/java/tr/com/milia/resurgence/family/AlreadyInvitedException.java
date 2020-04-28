package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class AlreadyInvitedException extends LocalizedException {
	public AlreadyInvitedException() {
		super("already.invited");
	}
}
