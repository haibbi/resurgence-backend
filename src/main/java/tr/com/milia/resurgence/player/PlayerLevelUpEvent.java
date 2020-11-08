package tr.com.milia.resurgence.player;

public class PlayerLevelUpEvent {
	private final String playerName;
	private final Title newTitle;

	public PlayerLevelUpEvent(String playerName, Title newTitle) {
		this.playerName = playerName;
		this.newTitle = newTitle;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Title getNewTitle() {
		return newTitle;
	}
}
