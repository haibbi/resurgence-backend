package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.item.Item;

import java.util.Map;

public class Status {
	private final MultiPlayerTask.Category category;
	private final Map<Item, Integer> items;
	private final boolean invited;
	private final boolean prepared;

	public Status(MultiPlayerTask.Category category, Map<Item, Integer> items, boolean invited, boolean prepared) {
		this.category = category;
		this.items = items;
		this.invited = invited;
		this.prepared = prepared;
	}

	public MultiPlayerTask.Category getCategory() {
		return category;
	}

	public Map<Item, Integer> getItems() {
		return items;
	}

	public boolean isInvited() {
		return invited;
	}

	public boolean isPrepared() {
		return prepared;
	}
}
