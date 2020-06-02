package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.skill.Skill;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

class TaskResponse implements LocaleEnum {

	private final Task task;
	private final Difficulty difficulty;
	private final Set<Skill> auxiliary;
	private final Set<Skill> skillGain;
	private final long moneyGain;
	private final int experienceGain;
	private final Duration duration;
	private final Duration left;
	private final Set<DropResponse> drop;
	private final Set<RequiredItemCategoryResponse> requiredItemCategory;

	public TaskResponse(Task task, Duration left) {
		this.task = task;
		this.difficulty = Difficulty.valueOf(task.getDifficulty());
		this.auxiliary = task.getAuxiliary();
		this.skillGain = task.getSkillGain();
		this.moneyGain = task.getMoneyGain();
		this.experienceGain = task.getExperienceGain();
		this.duration = task.getDuration();
		this.left = left;
		this.drop = task.getDrop()
			.entrySet()
			.stream()
			.map(e -> new DropResponse(e.getKey(), e.getValue()))
			.collect(Collectors.toSet());
		this.requiredItemCategory = task.getRequiredItemCategory()
			.entrySet()
			.stream()
			.map(e -> new RequiredItemCategoryResponse(e.getKey(), e.getValue()))
			.collect(Collectors.toSet());
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Set<Skill> getAuxiliary() {
		return auxiliary;
	}

	public Set<Skill> getSkillGain() {
		return skillGain;
	}

	public long getMoneyGain() {
		return moneyGain;
	}

	public int getExperienceGain() {
		return experienceGain;
	}

	public Duration getDuration() {
		return duration;
	}

	public Duration getLeft() {
		return left;
	}

	public Set<DropResponse> getDrop() {
		return drop;
	}

	public Set<RequiredItemCategoryResponse> getRequiredItemCategory() {
		return requiredItemCategory;
	}

	@Override
	public String[] getCodes() {
		return task.getCodes();
	}

	@Override
	public Object[] getArguments() {
		return task.getArguments();
	}

	@Override
	public String getDefaultMessage() {
		return task.getDefaultMessage();
	}

	@Override
	public String name() {
		return task.name();
	}

}


class RequiredItemCategoryResponse {
	private final Item.Category category;
	private final long quantity;

	public RequiredItemCategoryResponse(Item.Category category, long quantity) {
		this.category = category;
		this.quantity = quantity;
	}

	public Item.Category getCategory() {
		return category;
	}

	public long getQuantity() {
		return quantity;
	}
}
