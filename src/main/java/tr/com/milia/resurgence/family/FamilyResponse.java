package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.Race;

import java.util.Set;
import java.util.stream.Collectors;

public class FamilyResponse {
	private final String name;
	private final String boss;
	private final String consultant;
	private final Building building;
	private final Set<String> members;
	private final Race race;
	private Long bank;

	public FamilyResponse(Family family) {
		name = family.getName();
		boss = family.getBoss().getName();
		consultant = family.getConsultant().map(Player::getName).orElse(null);
		building = family.getBuilding();
		members = family.getMembers().stream().map(Player::getName).collect(Collectors.toSet());
		race = family.getRace();
	}

	static FamilyResponse exposed(Family family) {
		FamilyResponse response = new FamilyResponse(family);
		response.bank = family.getBank();
		return response;
	}

	public String getName() {
		return name;
	}

	public String getBoss() {
		return boss;
	}

	public String getConsultant() {
		return consultant;
	}

	public Building getBuilding() {
		return building;
	}

	public Set<String> getMembers() {
		return members;
	}

	public Race getRace() {
		return race;
	}

	public Long getBank() {
		return bank;
	}
}
