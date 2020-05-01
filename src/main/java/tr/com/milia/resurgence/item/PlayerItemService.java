package tr.com.milia.resurgence.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.task.TaskResult;
import tr.com.milia.resurgence.task.TaskStartedEvent;
import tr.com.milia.resurgence.task.TaskSucceedResult;

@Service
public class PlayerItemService {

	private static final Logger log = LoggerFactory.getLogger(PlayerItemService.class);

	private final PlayerItemRepository repository;

	public PlayerItemService(PlayerItemRepository repository) {
		this.repository = repository;
	}

	public void addItem(Player player, Item item, long quantity) {
		repository.findByPlayerAndItem(player, item).ifPresentOrElse(
			playerItem -> playerItem.add(quantity),
			() -> repository.save(new PlayerItem(player, item, quantity))
		);
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
			repository.findByPlayerAndItem(player, item).ifPresentOrElse(
				playerItem -> playerItem.add(quantity),
				() -> repository.save(new PlayerItem(player, item, quantity))
			);
		}

	}

	@EventListener(TaskStartedEvent.class)
	public void onTaskStartedEvent(TaskStartedEvent event) {
		var player = event.getPlayer();
		var requiredItemCategory = event.getTask().getRequiredItemCategory();
		var selectedItems = event.getSelectedItems();

		requiredItemCategory.forEach((category, count) -> {
			long quantity = selectedItems.keySet().stream()
				.filter(item -> item.getCategory().contains(category))
				.mapToLong(item -> repository.findByPlayerAndItem(player, item)
					.map(PlayerItem::getQuantity)
					.orElse(0L))
				.sum();
			// todo control if player have selected item
			if (quantity < count) throw new RequiredItemException(category.name());
		});
	}

	@EventListener(TaskResult.class)
	public void onTaskResult(TaskResult event) {
		// task başarılı veya başarısız olmasına bakmaksızın required itemlar silinir.
		var player = event.getPlayer();
		var usedItems = event.getUsedItems();

		usedItems.forEach((item, count) -> repository.findByPlayerAndItem(player, item)
			.orElseThrow(() -> {
				throw new RequiredItemException(item.name());
			}).remove(count));
	}
}
