package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.skill.Skill;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskResponse implements LocaleEnum {

	private static final String LOCATION_FORMAT = "static/task/%s.png";

	private final Task task;
	private final Difficulty difficulty;
	private final Set<Skill> auxiliary;
	private final Set<Skill> skillGain;
	private final long moneyGain;
	private final int experienceGain;
	private final long durationMills;
	private final Set<DropResponse> drop;
	private final Set<RequiredItemCategoryResponse> requiredItemCategory;
	private final String image;
	private final Long leftMillis;

	public TaskResponse(Task task) {
		this(task, Duration.ZERO);
	}

	public TaskResponse(Task task, Duration left) {
		this.task = task;
		difficulty = Difficulty.valueOf(task.getDifficulty());
		auxiliary = task.getAuxiliary();
		skillGain = task.getSkillGain();
		moneyGain = task.getMoneyGain();
		experienceGain = task.getExperienceGain();
		durationMills = task.getDuration().toMillis();
		leftMillis = left.toMillis();
		drop = task.getDrop()
			.entrySet()
			.stream()
			.map(e -> new DropResponse(e.getKey(), e.getValue()))
			.collect(Collectors.toSet());
		requiredItemCategory = task.getRequiredItemCategory()
			.entrySet()
			.stream()
			.map(e -> new RequiredItemCategoryResponse(e.getKey(), e.getValue()))
			.collect(Collectors.toSet());
		image = String.format(LOCATION_FORMAT, task.name());
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

	public long getDurationMills() {
		return durationMills;
	}

	public Long getLeftMillis() {
		return leftMillis;
	}

	public Set<DropResponse> getDrop() {
		return drop;
	}

	public Set<RequiredItemCategoryResponse> getRequiredItemCategory() {
		return requiredItemCategory;
	}

	public String getImage() {
		return image;
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
