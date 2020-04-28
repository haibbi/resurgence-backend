package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.item.Item;

import javax.validation.constraints.NotNull;
import java.util.Map;

class PrepareRequest {
	@NotNull
	MultiPlayerTask.Position position;

	Map<Item, Long> selectedItems;

	public void setPosition(MultiPlayerTask.Position position) {
		this.position = position;
	}

	public void setSelectedItems(Map<Item, Long> selectedItems) {
		this.selectedItems = selectedItems;
	}
}
