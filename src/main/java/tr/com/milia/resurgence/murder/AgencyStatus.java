package tr.com.milia.resurgence.murder;

import org.springframework.lang.Nullable;
import tr.com.milia.resurgence.player.Player;

import java.time.Instant;

class AgencyStatus {

	Player seeker;
	Player wanted;
	Status status;
	int agent;

	@Nullable
	Instant expire;

	@Nullable
	Instant left;

	private AgencyStatus(Player seeker, Player wanted, Status status, int agent, Instant expire, Instant left) {
		this.seeker = seeker;
		this.wanted = wanted;
		this.status = status;
		this.agent = agent;
		this.expire = expire;
		this.left = left;
	}

	public static AgencyStatus searching(Player seeker, Player wanted, int agent, Instant left) {
		return new AgencyStatus(seeker, wanted, Status.SEARCHING, agent, null, left);
	}

	public static AgencyStatus report(ResearchResult result) {
		Status status = result.isFound() ? Status.FOUND : Status.NOT_FOUND;
		return new AgencyStatus(result.getSeeker(),
			result.getWanted(),
			status,
			result.getAgent(),
			result.getExpireTime(),
			null);
	}

	public enum Status {
		SEARCHING,
		FOUND,
		NOT_FOUND,
	}

}
