package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.item.Item;

import java.util.Map;

public class Status {
	private final MultiPlayerTask.Position position;
	private final Map<Item, Long> items;
	private final boolean invited;
	private final boolean prepared;

	public Status(MultiPlayerTask.Position position, Map<Item, Long> items, boolean invited, boolean prepared) {
		this.position = position;
		this.items = items;
		this.invited = invited;
		this.prepared = prepared;
	}

	public MultiPlayerTask.Position getPosition() {
		return position;
	}

	public Map<Item, Long> getItems() {
		return items;
	}

	public boolean isInvited() {
		return invited;
	}

	public boolean isPrepared() {
		return prepared;
	}
}
