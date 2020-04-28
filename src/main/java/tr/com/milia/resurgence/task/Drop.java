package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;

public class Drop {
	private final Item item;
	private final long quantity;
	private final double ratio;

	private Drop(Item item, long quantity, double ratio) {
		this.item = item;
		this.quantity = quantity;
		this.ratio = ratio;
	}

	static Drop of(Item item, long quantity, double ratio) {
		return new Drop(item, quantity, ratio);
	}

	public Item getItem() {
		return item;
	}

	public long getQuantity() {
		return quantity;
	}

	public double getRatio() {
		return ratio;
	}
}
