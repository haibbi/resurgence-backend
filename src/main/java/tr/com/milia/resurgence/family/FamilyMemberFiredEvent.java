package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.player.Player;

public class FamilyMemberFiredEvent {
	private final Player removedMember;
	private final Family family;

	public FamilyMemberFiredEvent(Player removedMember, Family family) {
		this.removedMember = removedMember;
		this.family = family;
	}

	public Player getRemovedMember() {
		return removedMember;
	}

	public Family getFamily() {
		return family;
	}
}
