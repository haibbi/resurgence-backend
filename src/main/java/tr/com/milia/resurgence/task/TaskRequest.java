package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;

import java.util.Map;

class TaskRequest {
	Map<Item, Integer> selectedItems;

	public void setSelectedItems(Map<Item, Integer> selectedItems) {
		this.selectedItems = selectedItems;
	}
}
