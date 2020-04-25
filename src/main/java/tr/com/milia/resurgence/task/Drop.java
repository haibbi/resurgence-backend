package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;

public class Drop {
	private final Item item;
	private final int quantity;
	private final double ratio;

	private Drop(Item item, int quantity, double ratio) {
		this.item = item;
		this.quantity = quantity;
		this.ratio = ratio;
	}

	static Drop of(Item item, int quantity, double ratio) {
		return new Drop(item, quantity, ratio);
	}

	public Item getItem() {
		return item;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getRatio() {
		return ratio;
	}
}
