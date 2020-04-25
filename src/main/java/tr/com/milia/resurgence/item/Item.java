package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.skill.Skill;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public enum Item {

	KNIFE(BigDecimal.ZERO, Map.of(Skill.SNEAK, 15), Quality.COMMON, Set.of(Category.WEAPON));

	private final BigDecimal price;
	private final Map<Skill, Integer> skills;
	private final Quality quality;
	private final Set<Category> category;

	Item(BigDecimal price, Map<Skill, Integer> skills, Quality quality, Set<Category> category) {
		this.price = price;
		this.skills = skills;
		this.quality = quality;
		this.category = category;
	}

	public BigDecimal getPrice() {
		return price;
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

	public enum Category {

		WEAPON,

		VEHICLE,

	}
}
