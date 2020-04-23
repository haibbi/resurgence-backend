package tr.com.milia.resurgence.item;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.task.TaskResult;
import tr.com.milia.resurgence.task.TaskStartedEvent;
import tr.com.milia.resurgence.task.TaskSucceedResult;

@Service
public class PlayerItemService {

	private final PlayerItemRepository repository;

	public PlayerItemService(PlayerItemRepository repository) {
		this.repository = repository;
	}

	@EventListener(TaskSucceedResult.class)
	public void onTaskSucceedResult(TaskSucceedResult result) {
		var drop = result.getDrop();
		var player = result.getPlayer();

		for (var entry : drop.entrySet()) {
			Item item = entry.getKey();
			int quantity = entry.getValue();
			repository.findByPlayerAndItem(player, item).ifPresentOrElse(
				playerItem -> playerItem.add(quantity),
				() -> repository.save(new PlayerItem(player, item, quantity))
			);
		}

	}

	@EventListener(TaskStartedEvent.class)
	public void onTaskStartedEvent(TaskStartedEvent event) {
		var player = event.getPlayer();
		var requiredItems = event.getTask().getRequired();

		requiredItems.forEach((item, count) -> {
			int quantity = repository.findByPlayerAndItem(player, item)
				.map(PlayerItem::getQuantity)
				.orElse(0);
			if (quantity < count) throw new RequiredItemException(item.name());
		});
	}

	@EventListener(TaskResult.class)
	public void onTaskResult(TaskResult event) {
		// task başarılı veya başarısız olmasına bakmaksızın required itemlar silinir.
		var player = event.getPlayer();
		var requiredItems = event.getTask().getRequired();

		requiredItems.forEach((item, count) -> repository.findByPlayerAndItem(player, item)
			.orElseThrow(() -> {
				throw new RequiredItemException(item.name());
			}).remove(count));
	}
}
