package tr.com.milia.resurgence.player;

public class HonorPeriodCompletedEvent {
	private final String playerName;

	public HonorPeriodCompletedEvent(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}
}
