package tr.com.milia.resurgence.family;

public class FamilyBankEvent {

	private final Family family;
	private final Reason reason;
	private final long amount;

	public FamilyBankEvent(Family family, Reason reason, long amount) {
		this.family = family;
		this.reason = reason;
		this.amount = amount;
	}

	public Family getFamily() {
		return family;
	}

	public Reason getReason() {
		return reason;
	}

	public long getAmount() {
		return amount;
	}
}
