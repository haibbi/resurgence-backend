package tr.com.milia.resurgence.item;

public class PlayerItemResponse {

	private final ItemResponse item;
	private final long quantity;

	public PlayerItemResponse(PlayerItem playerItem) {
		item = new ItemResponse(playerItem.getItem());
		quantity = playerItem.getQuantity();
	}

	public ItemResponse getItem() {
		return item;
	}

	public long getQuantity() {
		return quantity;
	}
}
