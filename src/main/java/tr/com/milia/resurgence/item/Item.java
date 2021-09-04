package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.skill.Skill;
import tr.com.milia.resurgence.task.Task;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static tr.com.milia.resurgence.item.Item.Category.*;
import static tr.com.milia.resurgence.item.Quality.*;
import static tr.com.milia.resurgence.skill.Skill.*;

public enum Item implements LocaleEnum {

	// Money
	MONEY(1, emptyMap(), WORTHLESS, Set.of(Category.MONEY)),

	// Melee
	KNIFE(50, Map.of(SNEAK, 15), COMMON, Set.of(MELEE)),

	// Weapon
	GLOCK(100, Map.of(SNEAK, 20), COMMON, Set.of(WEAPON)),
	SHOTGUN(1000, Map.of(SNEAK, 25), RARE, Set.of(WEAPON)),

	// Bullet
	BULLET(300, emptyMap(), COMMON, Set.of(Category.BULLET)),

	// Vehicle
	FORD_FIESTA(10_000, emptyMap(), COMMON, Set.of(VEHICLE)),
	MUSTANG(100_000, emptyMap(), RARE, Set.of(VEHICLE)),

	// Guards
	JOE(100_000, Map.of(GUN_MASTERY, 10), COMMON, Set.of(GUARD)),

	// Health
	ASPIRIN(10_000, emptyMap(), WORTHLESS, Set.of(HEALTH), (player) -> {
		if (player.isHealthy()) return false;
		player.increaseHealth(10);
		return true;
	}),

	// No Category
	BEER(500, emptyMap(), WORTHLESS, emptySet()),
	AGENT(50_000, Map.of(TRACING, 10), COMMON, emptySet()),
	HOUSE(25_000, emptyMap(), WORTHLESS, emptySet()),
	;

	public static final Set<Item> PASSIVE = Set.of(JOE);
	private static Set<Item> FORBIDDEN_TO_BUY;
	private static Set<Item> SELLABLE;

	private final Map<Skill, Integer> skills;
	private final Quality quality;
	private final Set<Category> category;
	private final UsableItem usable;
	private long price;

	Item(long price, Map<Skill, Integer> skills, Quality quality, Set<Category> category) {
		this(price, skills, quality, category, null);
	}

	Item(long price, Map<Skill, Integer> skills, Quality quality, Set<Category> category, UsableItem usable) {
		this.price = price;
		this.skills = skills;
		this.quality = quality;
		this.category = category;
		this.usable = usable;
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

	public static Set<Item> sellable() {
		if (SELLABLE != null) return SELLABLE;

		return SELLABLE = Set.of(
			KNIFE, GLOCK, SHOTGUN, FORD_FIESTA, MUSTANG
		);
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

	Map<Skill, Integer> getSkills() {
		return skills;
	}

	public Set<Category> getCategory() {
		return category;
	}

	public Quality getQuality() {
		return quality;
	}

	public boolean isUsable() {
		return usable != null;
	}

	public boolean use(Player player) {
		return this.isUsable() && this.usable.useItem(player);
	}

	public enum Category implements LocaleEnum {
		MELEE,
		WEAPON,
		VEHICLE,
		MONEY,
		BULLET,
		GUARD,
		HEALTH,
	}
}
