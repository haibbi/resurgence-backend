package tr.com.milia.resurgence.task.multi;

class Invitation {
	final String leader;
	final MultiPlayerTask.Category category;

	public Invitation(String leader, MultiPlayerTask.Category category) {
		this.leader = leader;
		this.category = category;
	}

	public String getLeader() {
		return leader;
	}

	public MultiPlayerTask.Category getCategory() {
		return category;
	}
}
