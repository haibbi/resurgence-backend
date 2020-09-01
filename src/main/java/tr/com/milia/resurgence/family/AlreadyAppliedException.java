package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class AlreadyAppliedException extends LocalizedException {
	public AlreadyAppliedException() {
		super("already.applied");
	}
}
