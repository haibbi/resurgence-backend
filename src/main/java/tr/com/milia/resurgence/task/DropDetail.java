package tr.com.milia.resurgence.task;

public class DropDetail {

	private final long quantity;
	private final double ratio;

	private DropDetail(long quantity, double ratio) {
		this.quantity = quantity;
		this.ratio = ratio;
	}

	static DropDetail of(long quantity, double ratio) {
		return new DropDetail(quantity, ratio);
	}

	public long getQuantity() {
		return quantity;
	}

	public double getRatio() {
		return ratio;
	}
}
