package tr.com.milia.resurgence.murder;

class AttackResult {

	private final boolean succeed;
	private final boolean backfireSucceed;

	AttackResult(boolean succeed, boolean backfireSucceed) {
		this.succeed = succeed;
		this.backfireSucceed = backfireSucceed;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public boolean isBackfireSucceed() {
		return backfireSucceed;
	}
}
