package tr.com.milia.resurgence.task.multi;

class Invitation {
	final String leader;
	final MultiPlayerTask.Position position;

	public Invitation(String leader, MultiPlayerTask.Position position) {
		this.leader = leader;
		this.position = position;
	}

	public String getLeader() {
		return leader;
	}

	public MultiPlayerTask.Position getPosition() {
		return position;
	}
}
