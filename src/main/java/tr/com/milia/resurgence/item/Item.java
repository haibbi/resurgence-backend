package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.skill.Skill;
import tr.com.milia.resurgence.task.Task;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

public enum Item implements LocaleEnum {

	// Money
	MONEY(1, emptyMap(), Quality.WORTHLESS, Set.of(Category.MONEY)),

	// Weapon
	KNIFE(0, Map.of(Skill.SNEAK, 15), Quality.COMMON, Set.of(Category.WEAPON)),
	GLOCK(100, emptyMap(), Quality.COMMON, Set.of(Category.WEAPON)),

	// Vehicle
	FORD_FIESTA(10_000, emptyMap(), Quality.COMMON, Set.of(Category.VEHICLE)),
	MUSTANG(100_000, emptyMap(), Quality.RARE, Set.of(Category.VEHICLE)),

	// Guards
	JOE(100_000, Map.of(Skill.GUN_MASTERY, 10), Quality.COMMON, Set.of(Category.GUARD)),

	// No Category
	BEER(500, emptyMap(), Quality.WORTHLESS, emptySet()),
	BULLET(300, emptyMap(), Quality.COMMON, Set.of(Category.BULLET)),
	AGENT(50_000, Map.of(Skill.TRACING, 10), Quality.COMMON, emptySet()),
	HOUSE(25_000, emptyMap(), Quality.WORTHLESS, emptySet());

	public static final Set<Item> PASSIVE = Set.of(JOE);
	private static Set<Item> FORBIDDEN_TO_BUY;

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

	public static Set<Item> getForbiddenToBuy() {
		if (FORBIDDEN_TO_BUY != null) return FORBIDDEN_TO_BUY;

		FORBIDDEN_TO_BUY = Task.SMUGGLING_TASKS.stream()
			.map(Task::getDrop)
			.map(Map::keySet)
			.flatMap(Collection::stream)
			.collect(Collectors.toCollection(HashSet::new));
		FORBIDDEN_TO_BUY.add(MONEY);
		FORBIDDEN_TO_BUY.add(BULLET);
		FORBIDDEN_TO_BUY.add(AGENT);
		return FORBIDDEN_TO_BUY;
	}

	public Set<Category> getCategory() {
		return category;
	}

	public enum Category implements LocaleEnum {

		WEAPON,

		VEHICLE,

		MONEY,

		BULLET,

		GUARD,

	}
}
