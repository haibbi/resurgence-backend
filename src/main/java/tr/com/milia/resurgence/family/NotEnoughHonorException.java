package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class NotEnoughHonorException extends LocalizedException {
	public NotEnoughHonorException() {
		super("not.enough.honor");
	}
}
