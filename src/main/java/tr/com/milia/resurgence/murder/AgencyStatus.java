package tr.com.milia.resurgence.murder;

import org.springframework.lang.Nullable;
import tr.com.milia.resurgence.player.Player;

import java.time.Instant;

class AgencyStatus {

	Player seeker;
	Player wanted;
	Status status;

	@Nullable
	Instant expire;

	@Nullable
	Instant left;

	private AgencyStatus(Player seeker, Player wanted, Status status, Instant expire, Instant left) {
		this.seeker = seeker;
		this.wanted = wanted;
		this.status = status;
		this.expire = expire;
		this.left = left;
	}

	public static AgencyStatus searching(Player seeker, Player wanted, Instant left) {
		return new AgencyStatus(seeker, wanted, Status.SEARCHING, null, left);
	}

//	public static AgencyStatus found(Player seeker, Player wanted, Instant expire) {
//		return new AgencyStatus(seeker, wanted, Status.FOUND, expire, null);
//	}
//
//	public static AgencyStatus notFound(Player seeker, Player wanted, Instant expire) {
//		return new AgencyStatus(seeker, wanted, Status.NOT_FOUND, expire, null);
//	}

	public static AgencyStatus report(ResearchResult result) {
		Status status = result.isFound() ? Status.FOUND : Status.NOT_FOUND;
		return new AgencyStatus(result.getSeeker(), result.getWanted(), status, result.getExpireTime(), null);
	}

	public enum Status {
		SEARCHING,
		FOUND,
		NOT_FOUND,
	}

}
