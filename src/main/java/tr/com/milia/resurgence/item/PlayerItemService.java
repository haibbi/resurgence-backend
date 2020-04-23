package tr.com.milia.resurgence.item;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
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

}
