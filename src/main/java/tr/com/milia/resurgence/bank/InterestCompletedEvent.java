package tr.com.milia.resurgence.bank;

public class InterestCompletedEvent {

	private final String playerName;
	private final long amount;

	public InterestCompletedEvent(String playerName, long amount) {
		this.playerName = playerName;
		this.amount = amount;
	}

	public String getPlayerName() {
		return playerName;
	}

	public long getAmount() {
		return amount;
	}
}
