package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocalizedException;

public class RequiredItemException extends LocalizedException {

	private Item item;
	private Item.Category category;

	public RequiredItemException(Item item) {
		super("required.item");
		this.item = item;
	}

	public RequiredItemException(Item.Category category) {
		super("required.item.category");
		this.category = category;
	}

	public Item getItem() {
		return item;
	}

	public Item.Category getCategory() {
		return category;
	}
}
