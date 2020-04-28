package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;

import java.util.Map;

public class TaskFailedResult extends TaskResult {

	protected TaskFailedResult(Player player, Task task, Map<Item, Long> usedItems) {
		super(player, task, usedItems);
	}

}
