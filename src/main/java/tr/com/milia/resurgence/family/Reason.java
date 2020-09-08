package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.i18n.LocaleEnum;

public enum Reason implements LocaleEnum {
	DEPOSIT(true),
	WITHDRAW(false),
	BUILDING(false);

	private final boolean revenue;

	Reason(boolean revenue) {
		this.revenue = revenue;
	}

	public boolean isRevenue() {
		return revenue;
	}
}
