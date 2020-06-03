package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class BankTransferLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Player from;

	@ManyToOne
	private Player to;

	private long amount;

	private String description;

	private Instant time;

	public BankTransferLog() {
	}

	public BankTransferLog(Player from, Player to, long amount, String description) {
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.description = description;
		time = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public Player getFrom() {
		return from;
	}

	public Player getTo() {
		return to;
	}

	public long getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public Instant getTime() {
		return time;
	}
}
