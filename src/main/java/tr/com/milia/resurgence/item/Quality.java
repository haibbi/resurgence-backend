package tr.com.milia.resurgence.item;

public enum Quality {

	/**
	 * Worthless item quality
	 */
	WORTHLESS(0),

	/**
	 * Common item quality
	 */
	COMMON(1),

	/**
	 * Rare item quality
	 */
	RARE(2),

	/**
	 * Legendary item quality
	 */
	LEGENDARY(3);

	private final int factor;

	Quality(int factor) {
		this.factor = factor;
	}

	public int getFactor() {
		return factor;
	}
}
