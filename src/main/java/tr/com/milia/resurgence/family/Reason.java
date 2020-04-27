package tr.com.milia.resurgence.family;

public enum Reason {
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
