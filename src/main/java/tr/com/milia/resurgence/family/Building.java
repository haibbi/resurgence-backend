package tr.com.milia.resurgence.family;

/**
 * Do not change ordinal.
 */
public enum Building {

	HOME(10, 5_000_000),
	APARTMENT(25, 50_000_000),
	VILLA(50, 250_000_000),
	COMPLEX(100, 1_000_000_000);

	private final long size;
	private final long price;

	Building(long size, long price) {
		this.size = size;
		this.price = price;
	}

	public long getSize() {
		return size;
	}

	public long getPrice() {
		return price;
	}
}
