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
import static java.time.Duration.ofSeconds;
import static tr.com.milia.resurgence.skill.Skill.SNEAK;

public enum Task implements LocaleEnum {

	BANK_RUBBERY(150,
		Set.of(SNEAK),
		Set.of(SNEAK),
		BigDecimal.valueOf(50_000),
		1_000,
		ofSeconds(1),
		Set.of(Drop.of(Item.KNIFE, 1, .10)),
		Map.of(Item.Category.WEAPON, 1),
		true),

	HEIST_LEADER(100,
		Set.of(SNEAK),
		Set.of(SNEAK),
		BigDecimal.valueOf(100_000),
		10_000,
		ofDays(1),
		Set.of(Drop.of(Item.GLOCK, 1, .10)),
		Map.of(Item.Category.WEAPON, 1),
		false),

	HEIST_DRIVER(50,
		Set.of(SNEAK),
		Set.of(SNEAK),
		BigDecimal.valueOf(100_000),
		10_000,
		ofDays(1),
		Set.of(Drop.of(Item.FORD_FIESTA, 1, .10)),
		Map.of(Item.Category.VEHICLE, 1),
		false);

	private final int difficulty;
	private final Set<Skill> auxiliary;
	private final Set<Skill> skillGain;
	private final BigDecimal moneyGain;
	private final int experienceGain;
	private final Duration duration;
	private final Set<Drop> drop;

	/**
	 * Value değerleri sıfır olamaz! O halde required değildir.
	 */
	private final Map<Item.Category, Integer> requiredItemCategory;

	private final boolean performSolo;

	Task(int difficulty,
		 Set<Skill> auxiliary,
		 Set<Skill> skillGain,
		 BigDecimal moneyGain,
		 int experienceGain,
		 Duration duration,
		 Set<Drop> drop,
		 Map<Item.Category, Integer> requiredItemCategory,
		 boolean performSolo) {
		this.difficulty = difficulty;
		this.auxiliary = auxiliary;
		this.skillGain = skillGain;
		this.moneyGain = moneyGain;
		this.experienceGain = experienceGain;
		this.duration = duration;
		this.drop = drop;
		this.performSolo = performSolo;
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

	public Set<Drop> getDrop() {
		return Collections.unmodifiableSet(drop);
	}

	public Map<Item.Category, Integer> getRequiredItemCategory() {
		return Collections.unmodifiableMap(requiredItemCategory);
	}

	public boolean isPerformSolo() {
		return performSolo;
	}
}
