package tr.com.milia.resurgence.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.smuggling.SmugglingService;
import tr.com.milia.resurgence.task.Drop;
import tr.com.milia.resurgence.task.Task;
import tr.com.milia.resurgence.task.TaskResult;
import tr.com.milia.resurgence.task.TaskStartedEvent;
import tr.com.milia.resurgence.task.TaskSucceedResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerItemService {

	private static final Logger log = LoggerFactory.getLogger(PlayerItemService.class);
	private static final Set<Item> FORBIDDEN_TO_BUY = SmugglingService.SMUGGLING_TASKS.stream()
		.map(Task::getDrop)
		.flatMap(Collection::stream)
		.map(Drop::getItem)
		.collect(Collectors.toSet());

	private final PlayerItemRepository repository;
	private final PlayerService playerService;

	public PlayerItemService(PlayerItemRepository repository, PlayerService playerService) {
		this.repository = repository;
		this.playerService = playerService;
	}

	@Transactional
	public void addItem(Player player, Item item, long quantity) {
		repository.findByPlayerAndItem(player, item).ifPresentOrElse(
			playerItem -> playerItem.add(quantity),
			() -> repository.save(new PlayerItem(player, item, quantity))
		);
	}

	@Transactional
	public void sellItem(String playerName, Item item, long quantity) {
		Player player = findPlayer(playerName);
		PlayerItem playerItem = repository.findByPlayerAndItem(player, item).orElseThrow(ItemNotFoundException::new);

		playerItem.remove(quantity);
		long totalPrice = item.getPrice() * quantity;
		player.increaseBalance(totalPrice);
	}

	@Transactional
	public void buyItem(String playerName, Item item, long quantity) {
		if (FORBIDDEN_TO_BUY.contains(item)) throw new ForbiddenItemSoldException();

		Player player = findPlayer(playerName);
		long totalPrice = item.getPrice() * quantity;
		player.decreaseBalance(totalPrice);
		addItem(player, item, quantity);
	}

	public List<PlayerItem> findAllPlayerItem(String playerName) {
		Player player = findPlayer(playerName);
		return repository.findAllByPlayer(player);
	}

	@Transactional
	public void removeItem(Player player, Item item, long quantity) {
		PlayerItem playerItem = repository.findByPlayerAndItem(player, item).orElseThrow(ItemNotFoundException::new);
		playerItem.remove(quantity);
	}

	@EventListener(TaskSucceedResult.class)
	@Order(Ordered.HIGHEST_PRECEDENCE + 1)
	public void onTaskSucceedResult(TaskSucceedResult result) {
		log.debug("Task Succeed Result {}", result);
		var drop = result.getDrop();
		var player = result.getPlayer();

		for (var entry : drop) {
			Item item = entry.getItem();
			long quantity = entry.getQuantity();
			addItem(player, item, quantity);
		}

	}

	@EventListener(TaskStartedEvent.class)
	public void onTaskStartedEvent(TaskStartedEvent event) {
		var player = event.getPlayer();
		var requiredItemCategory = event.getTask().getRequiredItemCategory();
		var selectedItems = event.getSelectedItems();

		requiredItemCategory.forEach((category, requiredCount) -> {

			// Total number of selected item with filtered by required category
			long selectedCategoryItemCount = selectedItems.entrySet().stream()
				.filter(e -> e.getKey().getCategory().contains(category)) // The categories of the selected material
				// are filtered with the required category.
				.filter(e -> haveItem(player, e.getKey(), e.getValue())) // Control of the player's selected item
				.mapToLong(Map.Entry::getValue)
				.sum();

			if (selectedCategoryItemCount != requiredCount)
				throw new RequiredItemException(category);

		});
	}

	@EventListener(TaskResult.class)
	public void onTaskResult(TaskResult event) {
		// Whenever task succeed or failed, used item will be removed.
		var player = event.getPlayer();
		var usedItems = event.getUsedItems();
		usedItems.forEach((item, count) -> removeItem(player, item, count));
	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}

	private boolean haveItem(Player player, Item item, long quantity) {
		if (quantity == 0) return true;
		return repository.findByPlayerAndItemAndQuantityGreaterThanEqual(player, item, quantity).isPresent();
	}

}
