package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.skill.Skill;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TaskSucceedResult extends TaskResult {
	private final int experienceGain;
	private final BigDecimal moneyGain;
	private final Set<Skill> skillGain;
	private final Map<Item, Integer> drop;

	public TaskSucceedResult(Player player,
							 Task task,
							 int experienceGain,
							 BigDecimal moneyGain,
							 Set<Skill> skillGain,
							 Map<Item, Integer> drop,
							 Map<Item, Integer> usedItems) {
		super(player, task, usedItems);
		this.experienceGain = experienceGain;
		this.moneyGain = moneyGain;
		this.skillGain = skillGain;
		this.drop = drop;
	}

	public int getExperienceGain() {
		return experienceGain;
	}

	public BigDecimal getMoneyGain() {
		return moneyGain;
	}

	public Set<Skill> getSkillGain() {
		return skillGain == null ? Collections.emptySet() : Collections.unmodifiableSet(skillGain);
	}

	public Map<Item, Integer> getDrop() {
		return drop == null ? Collections.emptyMap() : Collections.unmodifiableMap(drop);
	}
}
