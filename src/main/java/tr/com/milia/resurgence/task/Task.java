package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Skill;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Set;

import static java.time.Duration.ofMinutes;
import static tr.com.milia.resurgence.player.Skill.SNEAK;

public enum Task implements LocaleEnum {

	BANK_RUBBERY(150,
		Set.of(SNEAK),
		Set.of(SNEAK),
		BigDecimal.valueOf(50_000),
		1_000,
		ofMinutes(1),
		Set.of(Item.KNIFE));
	// todo add drop item

	private final int difficulty;
	private final Set<Skill> auxiliary;
	private final Set<Skill> skillGain;
	private final BigDecimal moneyGain;
	private final int experienceGain;
	private final Duration duration;
	private final Set<Item> drop;

	Task(int difficulty,
		 Set<Skill> auxiliary,
		 Set<Skill> skillGain,
		 BigDecimal moneyGain,
		 int experienceGain,
		 Duration duration, Set<Item> drop) {
		this.difficulty = difficulty;
		this.auxiliary = auxiliary;
		this.skillGain = skillGain;
		this.moneyGain = moneyGain;
		this.experienceGain = experienceGain;
		this.duration = duration;
		this.drop = drop;
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

	public Set<Item> getDrop() {
		return Collections.unmodifiableSet(drop);
	}
}
