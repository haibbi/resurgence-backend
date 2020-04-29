package tr.com.milia.resurgence.bank;

import java.util.Date;

class InterestAccount {
	private final long amount;
	private final Date date;

	public InterestAccount(long amount, Date date) {
		this.amount = amount;
		this.date = date;
	}

	public long getAmount() {
		return amount;
	}

	public Date getDate() {
		return date;
	}
}
