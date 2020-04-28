package tr.com.milia.resurgence.family;

import java.time.Instant;

class FamilyBankLogResponse {

	private final String member;
	private final long amount;
	private final Reason reason;
	private final Instant date;

	FamilyBankLogResponse(FamilyBankLog log) {
		member = log.getMember();
		amount = log.getAmount();
		reason = log.getReason();
		date = log.getDate();
	}

	public String getMember() {
		return member;
	}

	public long getAmount() {
		return amount;
	}

	public Reason getReason() {
		return reason;
	}

	public Instant getDate() {
		return date;
	}
}
