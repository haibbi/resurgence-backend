package tr.com.milia.resurgence.item;

public class PlayerItemResponse {

	private final Item item;
	private final long quantity;

	public PlayerItemResponse(PlayerItem playerItem) {
		item = playerItem.getItem();
		quantity = playerItem.getQuantity();
	}

	public Item getItem() {
		return item;
	}

	public long getQuantity() {
		return quantity;
	}
}
