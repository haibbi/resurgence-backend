package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class ForbiddenItemSoldException extends LocalizedException {
	public ForbiddenItemSoldException() {
		super("forbidden.to.sell");
	}
}
