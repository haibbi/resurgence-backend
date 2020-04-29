package tr.com.milia.resurgence.family;

import java.time.Instant;

public class InvitationResponse {
	private final Long id;
	private final String player;
	private final String family;
	private final Instant time;
	private final Invitation.Direction direction;

	public InvitationResponse(Invitation invitation) {
		id = invitation.getId();
		player = invitation.getPlayer().getName();
		family = invitation.getFamily().getName();
		time = invitation.getTime();
		direction = invitation.getDirection();
	}

	public Long getId() {
		return id;
	}

	public String getPlayer() {
		return player;
	}

	public String getFamily() {
		return family;
	}

	public Instant getTime() {
		return time;
	}

	public Invitation.Direction getDirection() {
		return direction;
	}
}
