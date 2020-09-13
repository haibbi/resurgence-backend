package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.task.SelectedItem;

import javax.validation.constraints.NotNull;
import java.util.Set;

class MultiPlayerTaskRequest {

	@NotNull
	Set<SelectedItem> selectedItems;

	public void setSelectedItems(Set<SelectedItem> selectedItems) {
		this.selectedItems = selectedItems;
	}
}
