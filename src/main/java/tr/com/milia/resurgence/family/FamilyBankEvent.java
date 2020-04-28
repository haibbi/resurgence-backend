package tr.com.milia.resurgence.family;

public class FamilyBankEvent {
	private final Reason reason;
	private final long amount;

	public FamilyBankEvent(Reason reason, long amount) {
		this.reason = reason;
		this.amount = amount;
	}

	public Reason getReason() {
		return reason;
	}

	public long getAmount() {
		return amount;
	}
}
