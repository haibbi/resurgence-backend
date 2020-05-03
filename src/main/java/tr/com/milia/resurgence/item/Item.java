package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.skill.Skill;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

public enum Item {

	KNIFE(0, Map.of(Skill.SNEAK, 15), Quality.COMMON, Set.of(Category.WEAPON)),
	GLOCK(100, emptyMap(), Quality.COMMON, Set.of(Category.WEAPON)),
	MONEY(1, emptyMap(), Quality.WORTHLESS, Set.of(Category.MONEY)),
	BEER(500, emptyMap(), Quality.WORTHLESS, emptySet()),
	HOUSE(25_000, emptyMap(), Quality.WORTHLESS, emptySet()),
	FORD_FIESTA(10_000, emptyMap(), Quality.COMMON, Set.of(Category.VEHICLE));

	private final Map<Skill, Integer> skills;
	private final Quality quality;
	private final Set<Category> category;
	private long price;

	Item(long price, Map<Skill, Integer> skills, Quality quality, Set<Category> category) {
		this.price = price;
		this.skills = skills;
		this.quality = quality;
		this.category = category;
	}

	public long getPrice() {
		return price;
	}

	void setPrice(long value) {
		price = value;
	}

	public int getSkillsContribution(Set<Skill> useOnly) {
		return skills.entrySet()
			.stream()
			.filter(e -> useOnly.contains(e.getKey())) // filter skills
			.mapToInt(e -> e.getValue() * quality.getFactor()) // multiply by quality
			.sum();
	}

	public Set<Category> getCategory() {
		return category;
	}

	public enum Category implements LocaleEnum {

		WEAPON,

		VEHICLE,

		MONEY,

	}
}
