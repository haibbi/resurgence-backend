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
import tr.com.milia.resurgence.task.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static tr.com.milia.resurgence.item.Item.getForbiddenToBuy;

@Service
public class PlayerItemService {

	private static final Logger log = LoggerFactory.getLogger(PlayerItemService.class);

	private final PlayerItemRepository repository;
	private final PlayerService playerService;

	public PlayerItemService(PlayerItemRepository repository, PlayerService playerService) {
		this.repository = repository;
		this.playerService = playerService;
	}

	@Transactional
	public void addItem(Player player, Item item, long quantity) {
		repository.findById_PlayerAndId_Item(player, item).ifPresentOrElse(
			playerItem -> playerItem.add(quantity),
			() -> repository.save(new PlayerItem(player, item, quantity))
		);
	}

	@Transactional
	public void sellItem(String playerName, Item item, long quantity) {
		Player player = findPlayer(playerName);
		PlayerItem playerItem = repository.findById_PlayerAndId_Item(player, item).orElseThrow(ItemNotFoundException::new);

		playerItem.remove(quantity);
		long totalPrice = item.getPrice() * quantity;
		player.increaseBalance(totalPrice);
	}

	@Transactional
	public void buyItem(String playerName, Map<Item, Long> items) {
		if (!Collections.disjoint(getForbiddenToBuy(), items.keySet())) throw new ForbiddenItemSoldException();

		Player player = findPlayer(playerName);
		long totalPrice = items.entrySet().stream().mapToLong(e -> e.getKey().getPrice() * e.getValue()).sum();
		player.decreaseBalance(totalPrice);
		items.forEach((item, quantity) -> addItem(player, item, quantity));
	}

	public List<PlayerItem> findAllPlayerItem(String playerName) {
		Player player = findPlayer(playerName);
		return repository.findAllById_Player(player);
	}

	@Transactional
	public void removeItem(Player player, Item item, long quantity) {
		PlayerItem playerItem = repository.findById_PlayerAndId_Item(player, item).orElseThrow(ItemNotFoundException::new);
		playerItem.remove(quantity);
	}

	@EventListener(TaskSucceedResult.class)
	@Order(Ordered.HIGHEST_PRECEDENCE + 1)
	public void onTaskSucceedResult(TaskSucceedResult result) {
		log.debug("Task Succeed Result {}", result);
		Map<Item, DropDetail> drop = result.getDrop();
		var player = result.getPlayer();

		for (var entry : drop.entrySet()) {
			Item item = entry.getKey();
			long quantity = entry.getValue().getQuantity();
			addItem(player, item, quantity);
		}

	}

	@EventListener(value = TaskStartedEvent.class, condition = "#event.checkSelectedItem")
	public void onTaskStartedEvent(TaskStartedEvent event) {
		var player = event.getPlayer();
		var task = event.getTask();
		var selectedItems = event.getSelectedItems().entrySet()
			.stream()
			.map(e -> new SelectedItem(e.getKey(), e.getValue()))
			.collect(Collectors.toSet());
		checkItem(player, task, selectedItems);
	}

	public void checkItem(Player player, Task task, Set<SelectedItem> selectedItem) {
		var requiredItemCategory = task.getRequiredItemCategory();

		for (Map.Entry<Item.Category, Long> entry : requiredItemCategory.entrySet()) {
			Item.Category category = entry.getKey();
			Long requiredCount = entry.getValue();
			// Total number of selected item with filtered by required category
			long selectedCategoryItemCount = selectedItem.stream()
				.filter(si -> si.getItem().getCategory().contains(category)) // The categories of the selected material
				// are filtered with the required category.
				.filter(si -> haveItem(player, si.getItem(), si.getQuantity())) // Control of the player's selected item
				.mapToLong(SelectedItem::getQuantity)
				.sum();

			if (selectedCategoryItemCount != requiredCount)
				throw new RequiredItemException(category);
		}
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
		return repository.findById_PlayerAndId_ItemAndQuantityGreaterThanEqual(player, item, quantity).isPresent();
	}

}
