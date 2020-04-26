package tr.com.milia.resurgence.bank;

import java.util.Arrays;
import java.util.Optional;

public enum InterestRates {

	LOWEST(InterestRate.of(0, 100_000, .5)),
	LOW(InterestRate.of(100_001, 1_000_000, .4)),
	MEDIUM(InterestRate.of(1_000_001, 10_000_000, .3)),
	HIGH(InterestRate.of(10_000_001, 100_000_000, .2)),
	HIGHEST(InterestRate.of(100_000_000, 1_000_000_000, .1));

	private final InterestRate rate;

	InterestRates(InterestRate rate) {
		this.rate = rate;
	}

	public InterestRate getRate() {
		return rate;
	}

	static Optional<InterestRate> find(long amount) {
		return Arrays.stream(values())
			.map(InterestRates::getRate)
			.filter(rate -> amount >= rate.min || amount < rate.max)
			.findFirst();
	}

	static class InterestRate {
		final long min;
		final long max;
		final double ratio;

		private InterestRate(long min, long max, double ratio) {
			this.min = min;
			this.max = max;
			this.ratio = ratio;
		}

		static InterestRate of(long min, long max, double ratio) {
			return new InterestRate(min, max, ratio);
		}
	}
}
