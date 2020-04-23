package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.item.Item;

import javax.validation.constraints.NotNull;
import java.util.Map;

class PrepareRequest {
	@NotNull
	MultiPlayerTask.Position position;

	Map<Item, Integer> selectedItems;

	public void setPosition(MultiPlayerTask.Position position) {
		this.position = position;
	}

	public void setSelectedItems(Map<Item, Integer> selectedItems) {
		this.selectedItems = selectedItems;
	}
}
