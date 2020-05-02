package tr.com.milia.resurgence.estate;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class Deed {

	@Id
	@Enumerated(EnumType.STRING)
	private Building building;

	@ManyToOne(optional = false)
	private Player player;

	private Instant purchaseTime;

	public Deed() {
	}

	public Deed(Building building, Player player) {
		this.building = building;
		this.player = player;
		purchaseTime = Instant.now();
	}

	public Building getBuilding() {
		return building;
	}

	public Player getPlayer() {
		return player;
	}
}
