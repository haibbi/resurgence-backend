package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.i18n.LocaleEnum;

public enum Quality implements LocaleEnum {

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
