package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.task.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum MultiPlayerTask {

	HEIST(List.of(
		new Group(Position.LEADER, Task.HEIST_LEADER, 1),
		new Group(Position.DRIVER, Task.HEIST_DRIVER, 1)
	)),

	BANK(List.of(
		new Group(Position.LEADER, Task.BANK_LEADER, 1),
		new Group(Position.WEAPON_MASTER, Task.BANK_WEAPON_MASTER, 2),
		new Group(Position.DRIVER, Task.BANK_DRIVER, 1)
	)),
	;

	private final Map<Position, Task> positionTasks;
	private final Map<Position, Integer> quorum;

	MultiPlayerTask(List<Group> groups) {
		if (groups.stream().map(g -> g.position).noneMatch(g -> g.leader)) {
			throw new IllegalArgumentException("Group must have a one leader");
		}

		this.positionTasks = groups.stream().collect(Collectors.toMap(g -> g.position, g -> g.task));
		this.quorum = groups.stream().collect(Collectors.toMap(g -> g.position, g -> g.quorum));
	}

	public Task task(Position position) {
		return positionTasks.get(position);
	}

	public int quorum(Position position) {
		return quorum.get(position);
	}

	public Set<Position> positions() {
		return positionTasks.keySet();
	}

	public enum Position {
		LEADER(true), DRIVER, WEAPON_MASTER;

		private final boolean leader;

		Position() {
			this(false);
		}

		Position(boolean leader) {
			this.leader = leader;
		}

		public boolean isLeader() {
			return leader;
		}
	}

	public static class Group {
		private final Position position;
		private final Task task;
		private final int quorum;

		public Group(Position position, Task task, int quorum) {
			if (quorum < 1) throw new IllegalArgumentException("Quorum must more than 0.");
			if (position.leader && quorum != 1) throw new IllegalArgumentException("There is only one leader.");

			this.position = position;
			this.task = task;
			this.quorum = quorum;
		}
	}
}
