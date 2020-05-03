package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class ItemNotFoundException extends LocalizedException {
	public ItemNotFoundException() {
		super("item.not.found");
	}
}
