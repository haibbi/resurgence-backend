package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.task.TaskResult;

public class MultiplayerTaskResultEvent {

	private final String player;
	private final String leader;
	private final MultiPlayerTask.Position position;
	private final TaskResult result;

	public MultiplayerTaskResultEvent(String player, String leader, MultiPlayerTask.Position position, TaskResult result) {
		this.player = player;
		this.leader = leader;
		this.position = position;
		this.result = result;
	}

	public String getPlayer() {
		return player;
	}

	public String getLeader() {
		return leader;
	}

	public MultiPlayerTask.Position getPosition() {
		return position;
	}

	public TaskResult getResult() {
		return result;
	}
}
