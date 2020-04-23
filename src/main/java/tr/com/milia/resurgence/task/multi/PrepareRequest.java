package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.item.Item;

import javax.validation.constraints.NotNull;
import java.util.Map;

class PrepareRequest {
	@NotNull
	MultiPlayerTask.Category category;

	Map<Item, Integer> selectedItems;

	public void setCategory(MultiPlayerTask.Category category) {
		this.category = category;
	}

	public void setSelectedItems(Map<Item, Integer> selectedItems) {
		this.selectedItems = selectedItems;
	}
}
