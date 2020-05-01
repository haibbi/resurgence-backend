package tr.com.milia.resurgence.estate;

public class BuildingOwnerResponse {

	private final Building building;
	private final String owner;

	public BuildingOwnerResponse(Deed deed) {
		building = deed.getBuilding();
		owner = deed.getPlayer().getName();
	}

	public BuildingOwnerResponse(Building building) {
		this.building = building;
		owner = null;
	}

	public Building getBuilding() {
		return building;
	}

	public String getOwner() {
		return owner;
	}
}
