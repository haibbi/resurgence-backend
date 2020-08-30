package tr.com.milia.resurgence.family;

class FamilyBankResponse {
	private final long amount;

	FamilyBankResponse(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}
}
