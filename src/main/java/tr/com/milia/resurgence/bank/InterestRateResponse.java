package tr.com.milia.resurgence.bank;

class InterestRateResponse {
	private final long min;
	private final long max;
	private final double ratio;

	public InterestRateResponse(InterestRates.InterestRate rate) {
		min = rate.min;
		max = rate.max;
		ratio = rate.ratio;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	public double getRatio() {
		return ratio;
	}
}
