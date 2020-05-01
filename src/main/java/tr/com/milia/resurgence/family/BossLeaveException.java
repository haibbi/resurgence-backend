package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class BossLeaveException extends LocalizedException {
	public BossLeaveException() {
		super("boss.cant.leave.family");
	}
}
