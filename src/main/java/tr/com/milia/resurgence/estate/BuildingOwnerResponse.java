package tr.com.milia.resurgence.estate;

public class BuildingOwnerResponse {

	private final BuildingResponse building;
	private final String owner;

	public BuildingOwnerResponse(Deed deed) {
		building = new BuildingResponse(deed.getBuilding());
		owner = deed.getPlayer().getName();
	}

	public BuildingOwnerResponse(Building building) {
		this.building = new BuildingResponse(building);
		owner = null;
	}

	public BuildingResponse getBuilding() {
		return building;
	}

	public String getOwner() {
		return owner;
	}
}
