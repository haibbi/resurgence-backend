package tr.com.milia.resurgence.task;

public class Drop {

	private final long quantity;
	private final double ratio;

	private Drop(long quantity, double ratio) {
		this.quantity = quantity;
		this.ratio = ratio;
	}

	static Drop of(long quantity, double ratio) {
		return new Drop(quantity, ratio);
	}

	public long getQuantity() {
		return quantity;
	}

	public double getRatio() {
		return ratio;
	}
}
