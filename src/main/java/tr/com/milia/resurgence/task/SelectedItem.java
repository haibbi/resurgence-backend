package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectedItem {
	private final Item item;
	private final long quantity;

	public SelectedItem(Item item, long quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	static Set<SelectedItem> fromMap(Map<Item, Long> map) {
		if (map == null || map.isEmpty()) return Collections.emptySet();

		return map.entrySet().stream()
			.map(e -> new SelectedItem(e.getKey(), e.getValue()))
			.collect(Collectors.toSet());
	}

	public Item getItem() {
		return item;
	}

	public long getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "SelectedItem{" +
			"item=" + item +
			", quantity=" + quantity +
			'}';
	}
}
