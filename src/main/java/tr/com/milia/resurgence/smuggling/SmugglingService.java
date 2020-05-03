package tr.com.milia.resurgence.smuggling;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.item.PlayerItemService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.*;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SmugglingService extends TaskService {

	private final PlayerItemService playerItemService;

	public SmugglingService(PlayerService playerService,
							ApplicationEventPublisher eventPublisher,
							PlayerItemService playerItemService) {
		super(playerService, eventPublisher);
		this.playerItemService = playerItemService;
	}

	@Transactional
	public List<TaskResult> perform(Task task, String playerName, int repeat) {
		if (!task.isSmuggling()) throw new NotSmugglingTaskException();

		Player player = findPlayer(playerName);

		Long requiredMoneyForOne = task.getRequiredItemCategory().get(Item.Category.MONEY);
		long totalRequiredMoney = requiredMoneyForOne * repeat;

		playerItemService.addItem(player, Item.MONEY, totalRequiredMoney); // todo use buyItem
		player.decreaseBalance(totalRequiredMoney);

		TaskStartedEvent startedEvent = new TaskStartedEvent(player, task, Map.of(Item.MONEY, totalRequiredMoney));
		startedEvent.uncheckSelectedItem();
		eventPublisher.publishEvent(startedEvent);

		List<TaskResult> results = new LinkedList<>();
		for (int i = 0; i < repeat; i++) {
			TaskResult result = performInternal(task, player, Map.of(Item.MONEY, requiredMoneyForOne));
			results.add(result);
		}

		List<TaskResult> failedResults = results.stream()
			.filter(result -> result instanceof TaskFailedResult)
			.collect(Collectors.toList());

		List<TaskSucceedResult> succeedResults = results.stream()
			.filter(result -> result instanceof TaskSucceedResult)
			.map(result -> (TaskSucceedResult) result)
			.collect(Collectors.toList());

		aggregate(failedResults).ifPresent(eventPublisher::publishEvent);
		aggregateSucceed(succeedResults).ifPresent(eventPublisher::publishEvent);

		return results;
	}

	private Optional<TaskResult> aggregate(List<TaskResult> results) {
		Iterator<TaskResult> iterator = results.iterator();
		TaskResult initial = null;
		while (iterator.hasNext()) {
			if (initial == null) {
				initial = iterator.next();
				continue;
			}
			initial = initial.aggregate(iterator.next());
		}
		return Optional.ofNullable(initial);
	}

	private Optional<TaskSucceedResult> aggregateSucceed(List<TaskSucceedResult> results) {
		Iterator<TaskSucceedResult> iterator = results.iterator();
		TaskSucceedResult initial = null;
		while (iterator.hasNext()) {
			if (initial == null) {
				initial = iterator.next();
				continue;
			}
			initial = initial.aggregate(iterator.next());
		}
		return Optional.ofNullable(initial);
	}

}
