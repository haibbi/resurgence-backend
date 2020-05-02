package tr.com.milia.resurgence.bank;

class BankAccountResponse {

	private final Long amount;

	public BankAccountResponse(BankAccount account) {
		amount = account.getAmount();
	}

	public Long getAmount() {
		return amount;
	}
}
