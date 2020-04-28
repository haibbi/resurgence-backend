package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import javax.validation.constraints.Min;

// todo id'si player + item olabilir
@Entity
public class PlayerItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Item item;

	@Min(0)
	@Column(nullable = false)
	private long quantity;

	public PlayerItem() {
	}

	public PlayerItem(Player player, Item item, long quantity) {
		if (quantity < 0) throw new IllegalStateException();
		this.player = player;
		this.item = item;
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
		return item;
	}
}
