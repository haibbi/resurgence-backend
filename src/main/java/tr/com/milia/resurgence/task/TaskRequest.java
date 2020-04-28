package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.item.Item;

import java.util.Map;

class TaskRequest {
	Map<Item, Long> selectedItems;

	public void setSelectedItems(Map<Item, Long> selectedItems) {
		this.selectedItems = selectedItems;
	}
}
