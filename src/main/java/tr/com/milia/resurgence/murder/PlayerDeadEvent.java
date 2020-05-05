package tr.com.milia.resurgence.murder;

import tr.com.milia.resurgence.player.Player;

public class PlayerDeadEvent {

	private final Player player;

	public PlayerDeadEvent(Player playerName) {
		this.player = playerName;
	}

	public Player getPlayer() {
		return player;
	}
}
