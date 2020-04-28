package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.item.Item;

import java.util.Map;

class Preparation {
	final String leader;
	final MultiPlayerTask.Position position;
	final Map<Item, Long> selectedItems;

	Preparation(String leader, MultiPlayerTask.Position position, Map<Item, Long> selectedItems) {
		this.leader = leader;
		this.position = position;
		this.selectedItems = selectedItems;
	}

	public String getLeader() {
		return leader;
	}

	public MultiPlayerTask.Position getPosition() {
		return position;
	}

	public Map<Item, Long> getSelectedItems() {
		return selectedItems;
	}
}
