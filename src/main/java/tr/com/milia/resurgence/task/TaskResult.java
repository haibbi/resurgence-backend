package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;

import java.util.Collections;
import java.util.Map;

public abstract class TaskResult {

	private final Player player;
	private final Task task;
	private final Map<Item, Integer> usedItems;

	protected TaskResult(Player player, Task task, Map<Item, Integer> usedItems) {
		this.player = player;
		this.task = task;
		this.usedItems = usedItems;
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

	public Map<Item, Integer> getUsedItems() {
		return Collections.unmodifiableMap(usedItems);
	}
}
