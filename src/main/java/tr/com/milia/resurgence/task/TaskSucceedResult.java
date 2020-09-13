package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.skill.Skill;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TaskSucceedResult extends TaskResult {

	private final Set<Skill> skillGain;
	private int experienceGain;
	private long moneyGain;
	private Map<Item, DropDetail> drop;

	public TaskSucceedResult(Player player,
							 Task task,
							 int experienceGain,
							 long moneyGain,
							 Set<Skill> skillGain,
							 Map<Item, DropDetail> drop,
							 Map<Item, Long> usedItems) {
		super(player, task, usedItems);
		this.experienceGain = experienceGain;
		this.moneyGain = moneyGain;
		this.skillGain = skillGain;
		this.drop = drop;
	}

	public int getExperienceGain() {
		return experienceGain;
	}

	public long getMoneyGain() {
		return moneyGain;
	}

	public Set<Skill> getSkillGain() {
		return skillGain == null ? Collections.emptySet() : Collections.unmodifiableSet(skillGain);
	}

	public Map<Item, DropDetail> getDrop() {
		return drop == null ? Collections.emptyMap() : Collections.unmodifiableMap(drop);
	}

	public TaskSucceedResult aggregate(TaskSucceedResult result) {
		super.aggregate(result);
		this.experienceGain += result.experienceGain;
		this.moneyGain += result.moneyGain;
		// todo skill gain does not aggregate
		this.drop = new ConcurrentHashMap<>(this.drop);
		result.drop.forEach((item, resultDrop) ->
			this.drop.merge(item, resultDrop, (oldDrop, newDrop) ->
				DropDetail.of(oldDrop.getQuantity() + newDrop.getQuantity(), oldDrop.getRatio())));

		return this;
	}

	@Override
	public boolean isSucceed() {
		return true;
	}
}
