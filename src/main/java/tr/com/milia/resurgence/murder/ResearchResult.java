package tr.com.milia.resurgence.murder;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Duration;
import java.time.Instant;

@Entity
public class ResearchResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Player seeker;

	@ManyToOne
	private Player wanted;

	private boolean found;

	private Instant expireTime;

	public ResearchResult() {
	}

	public ResearchResult(Player seeker, Player wanted, boolean found) {
		this.seeker = seeker;
		this.wanted = wanted;
		this.found = found;
		expireTime = Instant.now().plus(Duration.ofDays(1));
	}

	public Player getSeeker() {
		return seeker;
	}

	public Player getWanted() {
		return wanted;
	}

	public boolean isFound() {
		return found;
	}

	public Instant getExpireTime() {
		return expireTime;
	}
}
