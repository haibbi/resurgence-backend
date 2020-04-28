package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;

class DropResponse {
	Item item;
	long quantity;

	public DropResponse(Drop drop) {
		item = drop.getItem();
		quantity = drop.getQuantity();
	}

	public Item getItem() {
		return item;
	}

	public long getQuantity() {
		return quantity;
	}
}
