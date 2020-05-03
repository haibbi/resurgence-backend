package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;

class DropResponse {
	Item item;
	long quantity;

	public DropResponse(Item item, DropDetail dropDetail) {
		this.item = item;
		quantity = dropDetail.getQuantity();
	}

	public Item getItem() {
		return item;
	}

	public long getQuantity() {
		return quantity;
	}
}
