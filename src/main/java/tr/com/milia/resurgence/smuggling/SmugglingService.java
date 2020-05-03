package tr.com.milia.resurgence.smuggling;

import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.item.PlayerItemService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.Task;
import tr.com.milia.resurgence.task.TaskResult;
import tr.com.milia.resurgence.task.TaskService;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class SmugglingService {

	private final TaskService taskService;
	private final PlayerService playerService;
	private final PlayerItemService playerItemService;

	public SmugglingService(TaskService taskService,
							PlayerService playerService,
							PlayerItemService playerItemService) {
		this.taskService = taskService;
		this.playerService = playerService;
		this.playerItemService = playerItemService;
	}

	@Transactional
	public List<TaskResult> perform(Task task, String playerName, int repeat) {
		if (!task.isSmuggling()) throw new NotSmugglingTaskException();

		Player player = playerService.findByName(playerName).orElseThrow(PlayerNotFound::new);

		long requiredMoney = task.getRequiredItemCategory().get(Item.Category.MONEY) * repeat;

		playerItemService.addItem(player, Item.MONEY, requiredMoney);
		player.decreaseBalance(requiredMoney);

		List<TaskResult> results = new LinkedList<>();
		for (int i = 0; i < repeat; i++) {
			TaskResult result = taskService.perform(task, playerName, Map.of(Item.MONEY, requiredMoney / repeat));
			results.add(result);
		}

		return results;
	}

}
