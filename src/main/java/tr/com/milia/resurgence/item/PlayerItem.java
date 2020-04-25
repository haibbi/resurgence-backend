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
	private int quantity;

	public PlayerItem() {
	}

	public PlayerItem(Player player, Item item, int quantity) {
		if (quantity < 0) throw new IllegalStateException();
		this.player = player;
		this.item = item;
		this.quantity = quantity;
	}

	public void add(int value) {
		quantity += value;
	}

	public void remove(int value) throws ItemOutOfBoundsException {
		if (quantity - value < 0) throw new ItemOutOfBoundsException();
		quantity -= value;
	}

	public int getQuantity() {
		return quantity;
	}

	public Item getItem() {
		return item;
	}
}
