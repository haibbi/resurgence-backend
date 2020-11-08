package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.skill.Skill;

import java.time.Duration;
import java.util.*;

import static java.time.Duration.*;
import static java.util.Collections.emptySet;
import static tr.com.milia.resurgence.skill.Skill.SNEAK;

public enum Task implements LocaleEnum {


	SHOPLIFTING(50,
		Set.of(SNEAK),
		Set.of(SNEAK),
		1_000,
		1_000,
		ofSeconds(10),
		Map.of(Item.KNIFE, DropDetail.of(1, .50)),
		Collections.emptyMap()),

	HOUSE_ROBBERY(75,
		Set.of(SNEAK),
		Set.of(SNEAK),
		5_000,
		2_000,
		ofMinutes(1),
		Map.of(
			Item.GLOCK, DropDetail.of(1, .10),
			Item.BULLET, DropDetail.of(5, .50)
		),
		Map.of(Item.Category.MELEE, 1L)),

	PARKING_LOT_ROBBERY(100,
		Set.of(SNEAK),
		Set.of(SNEAK),
		1_000,
		5_000,
		ofMinutes(2),
		Map.of(Item.FORD_FIESTA, DropDetail.of(1, .10)),
		Map.of(Item.Category.WEAPON, 1L)),

	BANK_RUBBERY(150,
		Set.of(SNEAK),
		Set.of(SNEAK),
		50_000,
		5_000,
		ofMinutes(2),
		Map.of(Item.SHOTGUN, DropDetail.of(1, .10)),
		Map.of(Item.Category.WEAPON, 1L, Item.Category.BULLET, 5L)),

	FEDERAL_RESERVE(201,
		Set.of(SNEAK),
		Set.of(SNEAK),
		105_000,
		10_000,
		ofMinutes(15),
		Map.of(
			Item.SHOTGUN, DropDetail.of(1, .80),
			Item.BULLET, DropDetail.of(15, .50)
		),
		Map.of(Item.Category.WEAPON, 2L, Item.Category.VEHICLE, 1L)),

	BEER_SMUGGLING(
		100,
		Set.of(SNEAK),
		emptySet(),
		0,
		100,
		Duration.ofSeconds(5),
		Map.of(Item.BEER, DropDetail.of(1, 1)),
		Map.of(Item.Category.MONEY, Item.BEER.getPrice())
	) {
		@Override
		public Map<Item.Category, Long> getRequiredItemCategory() {
			return Map.of(Item.Category.MONEY, Item.BEER.getPrice());
		}
	},

	HEIST_LEADER(100,
		Set.of(SNEAK),
		Set.of(SNEAK),
		100_000,
		10_000,
		ofDays(1),
		Map.of(Item.GLOCK, DropDetail.of(1, .10)),
		Map.of(Item.Category.WEAPON, 1L, Item.Category.BULLET, 100L)),

	HEIST_DRIVER(50,
		Set.of(SNEAK),
		Set.of(SNEAK),
		100_000,
		10_000,
		ofDays(1),
		Map.of(Item.FORD_FIESTA, DropDetail.of(1, .10)),
		Map.of(Item.Category.VEHICLE, 1L)),

	BANK_LEADER(50,
		Set.of(SNEAK),
		Set.of(SNEAK),
		100_000,
		10_000,
		ofDays(1),
		Map.of(Item.FORD_FIESTA, DropDetail.of(1, .10)),
		Map.of(Item.Category.WEAPON, 1L, Item.Category.BULLET, 200L)),

	BANK_DRIVER(50,
		Set.of(SNEAK),
		Set.of(SNEAK),
		100_000,
		10_000,
		ofDays(1),
		Map.of(Item.FORD_FIESTA, DropDetail.of(1, .10)),
		Map.of(Item.Category.VEHICLE, 1L)),

	BANK_WEAPON_MASTER(50,
		Set.of(SNEAK),
		Set.of(SNEAK),
		100_000,
		10_000,
		ofDays(1),
		Map.of(Item.FORD_FIESTA, DropDetail.of(1, .10)),
		Map.of(Item.Category.WEAPON, 2L, Item.Category.BULLET, 400L)),

	;

	public static final Set<Task> SMUGGLING_TASKS = Set.of(
		Task.BEER_SMUGGLING
	);

	public static final Set<Task> MULTI_PLAYER_TASKS = Set.of(
		Task.HEIST_LEADER,
		Task.HEIST_DRIVER,
		Task.BANK_LEADER,
		Task.BANK_WEAPON_MASTER,
		Task.BANK_DRIVER
	);

	public static final Set<Task> SOLO_TASKS;

	static {
		SOLO_TASKS = new HashSet<>(Arrays.asList(values()));
		SOLO_TASKS.removeAll(MULTI_PLAYER_TASKS);
		SOLO_TASKS.removeAll(SMUGGLING_TASKS);
	}

	private final int difficulty;
	private final Set<Skill> auxiliary;
	private final Set<Skill> skillGain;
	private final long moneyGain;
	private final int experienceGain;
	private final Duration duration;
	private final Map<Item, DropDetail> drop;

	/**
	 * Value must greater than 0, otherwise this is not required
	 */
	private final Map<Item.Category, Long> requiredItemCategory;

	Task(int difficulty,
		 Set<Skill> auxiliary,
		 Set<Skill> skillGain,
		 long moneyGain,
		 int experienceGain,
		 Duration duration,
		 Map<Item, DropDetail> drop,
		 Map<Item.Category, Long> requiredItemCategory) {
		this.difficulty = difficulty;
		this.auxiliary = auxiliary;
		this.skillGain = skillGain;
		this.moneyGain = moneyGain;
		this.experienceGain = experienceGain;
		this.duration = duration;
		this.drop = drop;
		if (requiredItemCategory.values().stream().anyMatch(i -> i <= 0)) {
			throw new IllegalStateException("Required item count should positive");
		}
		this.requiredItemCategory = requiredItemCategory;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public Set<Skill> getAuxiliary() {
		return Collections.unmodifiableSet(auxiliary);
	}

	public Set<Skill> getSkillGain() {
		return Collections.unmodifiableSet(skillGain);
	}

	public long getMoneyGain() {
		return moneyGain;
	}

	public int getExperienceGain() {
		return experienceGain;
	}

	public Duration getDuration() {
		return duration;
	}

	public Map<Item, DropDetail> getDrop() {
		return Collections.unmodifiableMap(drop);
	}

	public Map<Item.Category, Long> getRequiredItemCategory() {
		return Collections.unmodifiableMap(requiredItemCategory);
	}

	public boolean isSmuggling() {
		return SMUGGLING_TASKS.contains(this);
	}

	public boolean isMultiPlayer() {
		return MULTI_PLAYER_TASKS.contains(this);
	}

	public boolean isSolo() {
		return SOLO_TASKS.contains(this);
	}
}
