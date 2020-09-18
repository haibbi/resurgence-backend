package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class RequiredItemException extends LocalizedException {

	private final Item.Category category;

	public RequiredItemException(Item.Category category) {
		super("required.item.category");
		this.category = category;
	}

	public Item.Category getCategory() {
		return category;
	}
}
