package tr.com.milia.resurgence.task.multiplayer;

import java.time.Duration;
import java.time.Instant;

public class MemberCoolDownResponse extends AbstractMultiplayerTaskResultResponse {

	private final Instant time;

	protected MemberCoolDownResponse(String player, Duration duration) {
		super(player);
		this.time = Instant.now().plus(duration);
	}

	public Instant getTime() {
		return time;
	}

	@Override
	public boolean success() {
		return false;
	}
}
