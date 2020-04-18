package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.player.Skill;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public class TaskSucceedResult extends TaskResult {
	private final int experienceGain;
	private final BigDecimal moneyGain;
	private final Set<Skill> skillGain;

	public TaskSucceedResult(int experienceGain) {
		this(experienceGain, BigDecimal.ZERO, Collections.emptySet());
	}

	public TaskSucceedResult(int experienceGain, BigDecimal moneyGain) {
		this(experienceGain, moneyGain, Collections.emptySet());
	}

	public TaskSucceedResult(int experienceGain, BigDecimal moneyGain, Set<Skill> skillGain) {
		this.experienceGain = experienceGain;
		this.moneyGain = moneyGain;
		this.skillGain = skillGain;
	}

	public int getExperienceGain() {
		return experienceGain;
	}

	public BigDecimal getMoneyGain() {
		return moneyGain;
	}

	public Set<Skill> getSkillGain() {
		return Collections.unmodifiableSet(skillGain);
	}
}
