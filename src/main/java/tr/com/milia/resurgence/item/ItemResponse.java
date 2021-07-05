package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.skill.Skill;

import java.util.Map;
import java.util.Set;

public class ItemResponse implements LocaleEnum {
	private static final String LOCATION_FORMAT = "static/item/%s.png";

	private final Item item;
	private final Set<Item.Category> category;
	private final long price;
	private final String image;
	private final Quality quality;
	private final Map<Skill, Integer> skills;
	private final boolean usable;

	public ItemResponse(Item item) {
		this.item = item;
		this.category = item.getCategory();
		this.price = item.getPrice();
		this.image = String.format(LOCATION_FORMAT, item.name());
		this.quality = item.getQuality();
		this.skills = item.getSkills();
		this.usable = item.isUsable();
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

	public Quality getQuality() {
		return quality;
	}

	public Map<Skill, Integer> getSkills() {
		return skills;
	}

	public boolean isUsable() {
		return usable;
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
