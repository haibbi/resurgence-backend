package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.item.Item;

class MultiplayerTaskFailedResultResponse extends AbstractMultiplayerTaskResultResponse {

	private final Item.Category category;

	public MultiplayerTaskFailedResultResponse(String player, Item.Category category) {
		super(player);
		this.category = category;
	}

	public Item.Category getCategory() {
		return category;
	}

	@Override
	public boolean success() {
		return false;
	}
}
