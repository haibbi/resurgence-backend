package tr.com.milia.resurgence.murder;

public class AgencyJobCompletedEvent {

	private final String seeker;
	private final String wanted;
	private final int agent;

	public AgencyJobCompletedEvent(String seeker, String wanted, int agent) {
		this.seeker = seeker;
		this.wanted = wanted;
		this.agent = agent;
	}

	public String getSeeker() {
		return seeker;
	}

	public String getWanted() {
		return wanted;
	}

	public int getAgent() {
		return agent;
	}
}
