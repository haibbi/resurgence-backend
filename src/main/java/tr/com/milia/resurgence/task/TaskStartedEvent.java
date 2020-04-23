package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;

import java.util.Collections;
import java.util.Map;

public class TaskStartedEvent {

	private final Player player;
	private final Task task;
	private final Map<Item, Integer> selectedItems;

	public TaskStartedEvent(Player player, Task task, Map<Item, Integer> selectedItems) {
		this.player = player;
		this.task = task;
		this.selectedItems = selectedItems;
	}

	public Player getPlayer() {
		return player;
	}

	public Task getTask() {
		return task;
	}

	public Map<Item, Integer> getSelectedItems() {
		return Collections.unmodifiableMap(selectedItems);
	}
}
