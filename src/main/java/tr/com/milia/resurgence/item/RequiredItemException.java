package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class RequiredItemException extends LocalizedException {
	public RequiredItemException(String item) {
		super("required.item.category", item);
	}
}
