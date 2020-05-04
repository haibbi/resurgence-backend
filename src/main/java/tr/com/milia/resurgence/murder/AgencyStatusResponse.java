package tr.com.milia.resurgence.murder;

import java.time.Instant;

class AgencyStatusResponse {

	private final String seeker;
	private final String wanted;
	private final AgencyStatus.Status status;
	private final int agentCount;
	private final Instant expire;
	private final Instant left;

	public AgencyStatusResponse(AgencyStatus status) {
		seeker = status.seeker.getName();
		wanted = status.wanted.getName();
		this.status = status.status;
		agentCount = status.agent;
		expire = status.expire;
		left = status.left;
	}

	public String getSeeker() {
		return seeker;
	}

	public String getWanted() {
		return wanted;
	}

	public AgencyStatus.Status getStatus() {
		return status;
	}

	public int getAgentCount() {
		return agentCount;
	}

	public Instant getExpire() {
		return expire;
	}

	public Instant getLeft() {
		return left;
	}
}
