package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayerItemId implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Item item;

	public PlayerItemId() {
	}

	public PlayerItemId(Player player, Item item) {
		this.player = Objects.requireNonNull(player);
		this.item = Objects.requireNonNull(item);
	}

	public Player getPlayer() {
		return player;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlayerItemId that = (PlayerItemId) o;
		return Objects.equals(player, that.player) &&
			item == that.item;
	}

	@Override
	public int hashCode() {
		return Objects.hash(player, item);
	}
}
