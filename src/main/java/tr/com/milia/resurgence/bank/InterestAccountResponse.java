package tr.com.milia.resurgence.bank;

import java.time.Duration;
import java.time.Instant;

public class InterestAccountResponse {
	private final long amount;
	private final long deposit;
	private final long left;

	public InterestAccountResponse(InterestAccount account) {
		this.amount = account.getAmount();
		this.left = Duration.between(Instant.now(), account.getDate().toInstant()).toMillis();
		this.deposit = account.getDeposit();
	}

	public long getAmount() {
		return amount;
	}

	public long getDeposit() {
		return deposit;
	}

	public long getLeft() {
		return left;
	}
}
