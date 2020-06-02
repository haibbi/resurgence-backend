package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;

class SelectedItem {
	private final Item item;
	private final long quantity;

	public SelectedItem(Item item, long quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public long getQuantity() {
		return quantity;
	}
}
