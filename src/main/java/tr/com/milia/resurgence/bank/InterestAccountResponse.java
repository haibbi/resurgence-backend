package tr.com.milia.resurgence.bank;

import java.time.Duration;
import java.time.Instant;

public class InterestAccountResponse {
	private final long amount;
	private final Duration duration;

	public InterestAccountResponse(InterestAccount account) {
		this.amount = account.getAmount();
		Duration left = Duration.between(Instant.now(), account.getDate().toInstant());
		int nanosPart = left.toNanosPart();
		if (nanosPart != 0) {
			left = left.plusNanos(Duration.ofSeconds(1).getNano() - nanosPart);
		}
		this.duration = left;
	}

	public long getAmount() {
		return amount;
	}

	public Duration getDuration() {
		return duration;
	}
}
