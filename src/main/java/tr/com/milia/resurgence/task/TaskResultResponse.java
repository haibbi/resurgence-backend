package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.skill.Skill;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class TaskResultResponse {

	private final boolean succeed;
	private int experienceGain;
	private BigDecimal moneyGain;
	private Set<Skill> skillGain;
	private Map<Item, Integer> drop;

	public TaskResultResponse(TaskResult result) {
		this.succeed = result.isSucceed();
		if (result instanceof TaskSucceedResult succeedResult) {
			experienceGain = succeedResult.getExperienceGain();
			moneyGain = succeedResult.getMoneyGain();
			skillGain = succeedResult.getSkillGain();
			drop = succeedResult.getDrop();
		}
	}

	public boolean isSucceed() {
		return succeed;
	}

	public int getExperienceGain() {
		return experienceGain;
	}

	public BigDecimal getMoneyGain() {
		return moneyGain;
	}

	public Set<Skill> getSkillGain() {
		return skillGain;
	}

	public Map<Item, Integer> getDrop() {
		return drop;
	}
}
