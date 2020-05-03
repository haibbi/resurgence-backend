package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TaskResult {

	private final Player player;
	private final Task task;
	private Map<Item, Long> usedItems;

	protected TaskResult(Player player, Task task, Map<Item, Long> usedItems) {
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

	public Map<Item, Long> getUsedItems() {
		return Collections.unmodifiableMap(usedItems);
	}

	public TaskResult aggregate(TaskResult result) {
		this.usedItems = new ConcurrentHashMap<>(this.usedItems);
		result.usedItems.forEach((item, quantity) -> this.usedItems.merge(item, quantity, Long::sum));
		return this;
	}

}
