package tr.com.milia.resurgence.bank;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class BankAccountLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Player owner;

	private Instant time;

	private long change;

	private boolean increased;

	public BankAccountLog() {
	}

	public BankAccountLog(Player owner, long change, boolean increased) {
		this.owner = owner;
		this.change = change;
		this.increased = increased;
		time = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public Player getOwner() {
		return owner;
	}

	public Instant getTime() {
		return time;
	}

	public long getChange() {
		return change;
	}

	public boolean isIncreased() {
		return increased;
	}
}
