package tr.com.milia.resurgence.bank;

class BankAccountResponse {

	private final Long amount;

	public BankAccountResponse(BankAccount account) {
		amount = account.getAmount();
	}

	public BankAccountResponse(long amount) {
		this.amount = amount;
	}

	public Long getAmount() {
		return amount;
	}
}
