package tr.com.milia.resurgence.bank;

import java.util.Date;

class InterestAccount {
	private final long amount;
	private final long deposit;
	private final Date date;

	public InterestAccount(long amount, long deposit, Date date) {
		this.amount = amount;
		this.deposit = deposit;
		this.date = date;
	}

	public long getAmount() {
		return amount;
	}

	public long getDeposit() {
		return deposit;
	}

	public Date getDate() {
		return date;
	}
}
