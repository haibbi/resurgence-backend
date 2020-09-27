package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocaleEnum;

import java.util.Set;

public class ItemResponse implements LocaleEnum {
	private static final String LOCATION_FORMAT = "static/item/%s.png";

	private final Item item;
	private final Set<Item.Category> category;
	private final long price;
	private final String image;

	public ItemResponse(Item item) {
		this.item = item;
		category = item.getCategory();
		price = item.getPrice();
		image = String.format(LOCATION_FORMAT, item.name());
	}

	public Set<Item.Category> getCategory() {
		return category;
	}

	public long getPrice() {
		return price;
	}

	public String getImage() {
		return image;
	}

	@Override
	public String[] getCodes() {
		return item.getCodes();
	}

	@Override
	public Object[] getArguments() {

		return item.getArguments();
	}

	@Override
	public String getDefaultMessage() {
		return item.getDefaultMessage();
	}

	@Override
	public String name() {
		return item.name();
	}
}
