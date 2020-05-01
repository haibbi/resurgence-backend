package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class ItemOutOfBoundsException extends LocalizedException {
	public ItemOutOfBoundsException() {
		super("item.out.of.bounds");
	}
}
