package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.item.Item;

import java.util.Map;

class Preparation {
	final String leader;
	final MultiPlayerTask.Category category;
	final Map<Item, Integer> selectedItems;

	Preparation(String leader, MultiPlayerTask.Category category, Map<Item, Integer> selectedItems) {
		this.leader = leader;
		this.category = category;
		this.selectedItems = selectedItems;
	}

	public String getLeader() {
		return leader;
	}

	public MultiPlayerTask.Category getCategory() {
		return category;
	}

	public Map<Item, Integer> getSelectedItems() {
		return selectedItems;
	}
}
