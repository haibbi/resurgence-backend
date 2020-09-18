package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class NotAllMemberReadyException extends LocalizedException {
	public NotAllMemberReadyException() {
		super("not.all.member.ready");
	}
}
