package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.player.Player;

public class TaskStartedEvent {

	private final Player player;
	private final Task task;

	public TaskStartedEvent(Player player, Task task) {
		this.player = player;
		this.task = task;
	}

	public Player getPlayer() {
		return player;
	}

	public Task getTask() {
		return task;
	}
}
