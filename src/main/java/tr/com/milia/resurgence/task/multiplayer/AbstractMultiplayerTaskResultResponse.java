package tr.com.milia.resurgence.task.multiplayer;

abstract class AbstractMultiplayerTaskResultResponse {

	private final String player;

	protected AbstractMultiplayerTaskResultResponse(String player) {
		this.player = player;
	}

	public String getPlayer() {
		return player;
	}

	public abstract boolean success();
}
