package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.skill.Skill;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

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
		Map.of(Item.KNIFE, 1));

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
	private final Map<Item, Integer> required;

	Task(int difficulty,
		 Set<Skill> auxiliary,
		 Set<Skill> skillGain,
		 BigDecimal moneyGain,
		 int experienceGain,
		 Duration duration,
		 Map<Item, Integer> drop,
		 Map<Item, Integer> required) {
		this.difficulty = difficulty;
		this.auxiliary = auxiliary;
		this.skillGain = skillGain;
		this.moneyGain = moneyGain;
		this.experienceGain = experienceGain;
		this.duration = duration;
		this.drop = drop;
		if (required.values().stream().anyMatch(i -> i <= 0)) {
			throw new IllegalStateException("Required item count should positive");
		}
		this.required = required;
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

	public Map<Item, Integer> getRequired() {
		return Collections.unmodifiableMap(required);
	}
}
