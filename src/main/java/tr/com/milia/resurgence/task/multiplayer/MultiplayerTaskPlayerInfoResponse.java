package tr.com.milia.resurgence.task.multiplayer;

import static tr.com.milia.resurgence.task.TaskResponse.LOCATION_FORMAT;

public class MultiplayerTaskPlayerInfoResponse {

	private final MultiPlayerTask task;
	private final boolean perform;

	public MultiplayerTaskPlayerInfoResponse(MultiPlayerTask task, boolean perform) {
		this.task = task;
		this.perform = perform;
	}

	public MultiPlayerTask getTask() {
		return task;
	}

	public boolean isPerform() {
		return perform;
	}

	public String getImage() {
		return String.format(LOCATION_FORMAT, task.name());
	}
}
