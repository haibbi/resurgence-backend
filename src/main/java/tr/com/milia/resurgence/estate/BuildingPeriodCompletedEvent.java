package tr.com.milia.resurgence.estate;

public class BuildingPeriodCompletedEvent {

	final String playerName;
	final Building building;

	public BuildingPeriodCompletedEvent(String playerName, Building building) {
		this.playerName = playerName;
		this.building = building;
	}

}
