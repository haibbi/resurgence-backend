package tr.com.milia.resurgence.stock;

import tr.com.milia.resurgence.item.Item;

public class StockEvent {
	private final Item item;
	private final int change;

	public StockEvent(Item item, int change) {
		this.item = item;
		this.change = change;
	}

	public Item getItem() {
		return item;
	}

	public int getChange() {
		return change;
	}
}
