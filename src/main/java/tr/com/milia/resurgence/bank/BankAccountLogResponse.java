package tr.com.milia.resurgence.bank;

import java.time.Instant;

class BankAccountLogResponse {

	private final Instant time;
	private final long change;
	private final boolean increased;

	public BankAccountLogResponse(BankAccountLog log) {
		time = log.getTime();
		change = log.getChange();
		increased = log.isIncreased();
	}

	public Instant getTime() {
		return time;
	}

	public long getChange() {
		return change;
	}

	public boolean isIncreased() {
		return increased;
	}
}
