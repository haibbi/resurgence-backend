package tr.com.milia.resurgence.family;

import org.springframework.lang.Nullable;
import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.Race;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FamilyResponse {
	private final String name;
	private final FamilyBuildingResponse building;
	private final Race race;
	private final String image;
	private final int size;

	private String boss;
	private String consultant;
	private Set<String> members;
	private List<ChiefResponse> chiefs;

	public FamilyResponse(Family family) {
		this(family, null);
	}

	private FamilyResponse(Family family, @Nullable Integer size) {
		name = family.getName();
		building = new FamilyBuildingResponse(family.getBuilding());
		race = family.getRace();
		image = family.getImage();
		this.size = size == null ? family.getMembers().size() : size;
	}

	public static FamilyResponse exposed(Family family) {
		Set<String> members = family.getMembers().stream().map(Player::getName).collect(Collectors.toSet());
		FamilyResponse response = new FamilyResponse(family, members.size());
		response.boss = family.getBoss().getName();
		response.consultant = family.getConsultant().map(Player::getName).orElse(null);
		response.members = members;
		response.chiefs = family.getChiefs().stream().map(ChiefResponse::new).collect(Collectors.toList());
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

	public List<ChiefResponse> getChiefs() {
		return chiefs;
	}

	public FamilyBuildingResponse getBuilding() {
		return building;
	}

	public Set<String> getMembers() {
		return members;
	}

	public Race getRace() {
		return race;
	}

	public String getImage() {
		return image;
	}

	public int getSize() {
		return size;
	}
}

class ChiefResponse {

	private final String name;
	private final Set<String> members;

	public ChiefResponse(Chief chief) {
		name = chief.getChief().getName();
		members = chief.getMembers().stream().map(Player::getName).collect(Collectors.toSet());
	}

	public String getName() {
		return name;
	}

	public Set<String> getMembers() {
		return members;
	}
}

class FamilyBuildingResponse implements LocaleEnum {

	private final Building building;
	private final long size;
	private final long price;

	FamilyBuildingResponse(Building building) {
		this.building = building;
		size = building.getSize();
		price = building.getPrice();
	}

	public long getSize() {
		return size;
	}

	public long getPrice() {
		return price;
	}

	@Override
	public String[] getCodes() {
		return building.getCodes();
	}

	@Override
	public Object[] getArguments() {
		return building.getArguments();
	}

	@Override
	public String getDefaultMessage() {
		return building.getDefaultMessage();
	}

	@Override
	public String name() {
		return building.name();
	}
}
