package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.skill.Skill;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TaskSucceedResult extends TaskResult {
	private final int experienceGain;
	private final int moneyGain;
	private final Set<Skill> skillGain;
	private final Set<Drop> drop;

	public TaskSucceedResult(Player player,
							 Task task,
							 int experienceGain,
							 int moneyGain,
							 Set<Skill> skillGain,
							 Set<Drop> drop,
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

	public int getMoneyGain() {
		return moneyGain;
	}

	public Set<Skill> getSkillGain() {
		return skillGain == null ? Collections.emptySet() : Collections.unmodifiableSet(skillGain);
	}

	public Set<Drop> getDrop() {
		return drop == null ? Collections.emptySet() : Collections.unmodifiableSet(drop);
	}
}
