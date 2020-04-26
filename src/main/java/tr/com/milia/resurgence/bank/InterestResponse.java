package tr.com.milia.resurgence.bank;

class InterestResponse {
	private final long amount;

	public InterestResponse(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}
}
