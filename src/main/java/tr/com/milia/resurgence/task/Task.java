package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.skill.Skill;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static java.time.Duration.ofDays;
import static java.time.Duration.ofMinutes;
import static tr.com.milia.resurgence.skill.Skill.SNEAK;

public enum Task implements LocaleEnum {

	BANK_RUBBERY(150,
		Set.of(SNEAK),
		Set.of(SNEAK),
		BigDecimal.valueOf(50_000),
		1_000,
		ofMinutes(0),
		Map.of(Item.KNIFE, 1),
		Map.of(Item.Category.WEAPON, 1)),
	HEIST_LEADER(100, Set.of(SNEAK), Set.of(SNEAK), BigDecimal.valueOf(100_000), 10_000,
		ofDays(1), Map.of(Item.GLOCK, 1), Map.of(Item.Category.WEAPON, 1)),
	HEIST_DRIVER(50, Set.of(SNEAK), Set.of(SNEAK), BigDecimal.valueOf(100_000), 10_000,
		ofDays(1), Map.of(Item.FORD_FIESTA, 1), Map.of(Item.Category.VEHICLE, 1));

	private final int difficulty;
	private final Set<Skill> auxiliary;
	private final Set<Skill> skillGain;
	private final BigDecimal moneyGain;
	private final int experienceGain;
	private final Duration duration;
	private final Map<Item, Integer> drop;

	/**
	 * Value değerleri sıfır olamaz! O halde required değildir.
	 */
	private final Map<Item.Category, Integer> requiredItemCategory;

	Task(int difficulty,
		 Set<Skill> auxiliary,
		 Set<Skill> skillGain,
		 BigDecimal moneyGain,
		 int experienceGain,
		 Duration duration,
		 Map<Item, Integer> drop,
		 Map<Item.Category, Integer> requiredItemCategory) {
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

	public BigDecimal getMoneyGain() {
		return moneyGain;
	}

	public int getExperienceGain() {
		return experienceGain;
	}

	public Duration getDuration() {
		return duration;
	}

	public Map<Item, Integer> getDrop() {
		return Collections.unmodifiableMap(drop);
	}

	public Map<Item.Category, Integer> getRequiredItemCategory() {
		return Collections.unmodifiableMap(requiredItemCategory);
	}
}
