package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

@Entity
public class PlayerItem {

	@EmbeddedId
	private PlayerItemId id;

	@Min(0)
	@Column(nullable = false)
	private long quantity;

	public PlayerItem() {
	}

	public PlayerItem(Player player, Item item, long quantity) {
		if (quantity < 0) throw new IllegalStateException();
		this.id = new PlayerItemId(player, item);
		this.quantity = quantity;
	}

	public void add(long value) {
		quantity += value;
	}

	public void remove(long value) throws ItemOutOfBoundsException {
		if (value > quantity) throw new ItemOutOfBoundsException();
		quantity -= value;
	}

	public long getQuantity() {
		return quantity;
	}

	public Item getItem() {
		return id.getItem();
	}

	boolean isUsable() {
		return id.getItem().isUsable() && quantity > 0;
	}

	boolean use(Player player) {
		if (!isUsable()) return false;
		boolean used = id.getItem().use(player);
		if (used) quantity--;
		return used;
	}

}
