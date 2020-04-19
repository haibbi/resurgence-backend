package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.player.Player;

public abstract class TaskResult {

	private final Player player;
	private final Task task;

	protected TaskResult(Player player, Task task) {
		this.player = player;
		this.task = task;
	}

	public boolean isSucceed() {
		return this instanceof TaskSucceedResult;
	}

	public Player getPlayer() {
		return player;
	}

	public Task getTask() {
		return task;
	}
}
