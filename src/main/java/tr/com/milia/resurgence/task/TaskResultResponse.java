package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.skill.Skill;

import java.util.Set;
import java.util.stream.Collectors;

public class TaskResultResponse {

	private final boolean succeed;
	private int experienceGain;
	private int moneyGain;
	private Set<Skill> skillGain;
	private Set<DropResponse> drop;

	public TaskResultResponse(TaskResult result) {
		this.succeed = result.isSucceed();
		if (result instanceof TaskSucceedResult) {
			var succeedResult = (TaskSucceedResult) result;
			experienceGain = succeedResult.getExperienceGain();
			moneyGain = succeedResult.getMoneyGain();
			skillGain = succeedResult.getSkillGain();
			drop = succeedResult.getDrop().stream().map(DropResponse::new).collect(Collectors.toSet());
		}
	}

	public boolean isSucceed() {
		return succeed;
	}

	public int getExperienceGain() {
		return experienceGain;
	}

	public int getMoneyGain() {
		return moneyGain;
	}

	public Set<Skill> getSkillGain() {
		return skillGain;
	}

	public Set<DropResponse> getDrop() {
		return drop;
	}
}
