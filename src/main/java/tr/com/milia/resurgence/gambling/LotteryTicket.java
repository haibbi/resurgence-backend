package tr.com.milia.resurgence.gambling;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class LotteryTicket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Player player;

	@Column(nullable = false, updatable = false)
	private Instant purchaseTime;

	public LotteryTicket() {
		purchaseTime = Instant.now();
	}

	public LotteryTicket(Player player) {
		this();
		this.player = player;
	}

	public Long getId() {
		return id;
	}

	public Player getPlayer() {
		return player;
	}
}
