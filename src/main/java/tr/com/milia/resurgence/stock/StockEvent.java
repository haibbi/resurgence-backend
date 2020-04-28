package tr.com.milia.resurgence.stock;

import tr.com.milia.resurgence.item.Item;

public class StockEvent {
	private final Item item;
	private final long change;

	public StockEvent(Item item, long change) {
		this.item = item;
		this.change = change;
	}

	public Item getItem() {
		return item;
	}

	public long getChange() {
		return change;
	}
}
