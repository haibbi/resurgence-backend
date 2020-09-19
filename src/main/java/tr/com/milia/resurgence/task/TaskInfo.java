package tr.com.milia.resurgence.task;

import java.util.Set;

class TaskInfo {
	private final String player;
	private final Task task;
	private final Set<SelectedItem> selectedItems;
	private final double skillContribution;
	private final double itemContribution;
	private final double passiveItemContribution;
	private final double successRatio;
	private final double randomRatio;
	private final double gainRatio;
	private final boolean succeed;

	private TaskInfo(String player,
					 Task task,
					 Set<SelectedItem> selectedItems,
					 double skillContribution,
					 double itemContribution,
					 double passiveItemContribution,
					 double successRatio,
					 double randomRatio,
					 double gainRatio,
					 boolean succeed) {
		this.player = player;
		this.task = task;
		this.selectedItems = selectedItems;
		this.skillContribution = skillContribution;
		this.itemContribution = itemContribution;
		this.passiveItemContribution = passiveItemContribution;
		this.successRatio = successRatio;
		this.randomRatio = randomRatio;
		this.gainRatio = gainRatio;
		this.succeed = succeed;
	}

	@Override
	public String toString() {
		return "TaskInfo{" +
			"player='" + player + '\'' +
			", task=" + task +
			", selectedItems=" + selectedItems +
			", skillContribution=" + skillContribution +
			", itemContribution=" + itemContribution +
			", passiveItemContribution=" + passiveItemContribution +
			", successRatio=" + successRatio +
			", randomRatio=" + randomRatio +
			", gainRatio=" + gainRatio +
			", succeed=" + succeed +
			'}';
	}

	public static class Builder {
		private String player;
		private Task task;
		private Set<SelectedItem> selectedItems;
		private double skillContribution;
		private double itemContribution;
		private double passiveItemContribution;
		private double successRatio;
		private double randomRatio;
		private double gainRatio;
		private boolean succeed;

		private Builder() {
		}

		public static Builder newBuilder() {
			return new Builder();
		}

		public Builder player(String player) {
			this.player = player;
			return this;
		}

		public Builder task(Task task) {
			this.task = task;
			return this;
		}

		public Builder selectedItems(Set<SelectedItem> selectedItems) {
			this.selectedItems = selectedItems;
			return this;
		}

		public Builder skillContribution(double skillContribution) {
			this.skillContribution = skillContribution;
			return this;
		}

		public Builder itemContribution(double itemContribution) {
			this.itemContribution = itemContribution;
			return this;
		}

		public Builder passiveItemContribution(double passiveItemContribution) {
			this.passiveItemContribution = passiveItemContribution;
			return this;
		}

		public Builder successRatio(double successRatio) {
			this.successRatio = successRatio;
			return this;
		}

		public Builder randomRatio(double randomRatio) {
			this.randomRatio = randomRatio;
			return this;
		}

		public Builder gainRatio(double gainRatio) {
			this.gainRatio = gainRatio;
			return this;
		}

		public Builder succeed(boolean succeed) {
			this.succeed = succeed;
			return this;
		}

		public TaskInfo build() {
			return new TaskInfo(player,
				task,
				selectedItems,
				skillContribution,
				itemContribution,
				passiveItemContribution,
				successRatio,
				randomRatio,
				gainRatio,
				succeed);
		}
	}

}
