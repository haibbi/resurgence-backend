package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.task.TaskResult;
import tr.com.milia.resurgence.task.TaskResultResponse;

class MultiplayerTaskSucceededResultResponse extends AbstractMultiplayerTaskResultResponse {

	private final TaskResultResponse result;

	protected MultiplayerTaskSucceededResultResponse(String player, TaskResult result) {
		super(player);
		this.result = new TaskResultResponse(result);
	}

	public TaskResultResponse getResult() {
		return result;
	}

	@Override
	public boolean success() {
		return true;
	}
}
