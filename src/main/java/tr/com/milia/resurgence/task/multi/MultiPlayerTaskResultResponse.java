package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.task.TaskResult;
import tr.com.milia.resurgence.task.TaskResultResponse;

import java.util.Map;
import java.util.stream.Collectors;

class MultiPlayerTaskResultResponse {

	private final Map<String, TaskResultResponse> taskResult;

	public MultiPlayerTaskResultResponse(Map<String, TaskResult> taskResult) {
		this.taskResult = taskResult.entrySet().stream()
			.map(e -> Map.entry(e.getKey(), new TaskResultResponse(e.getValue())))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, TaskResultResponse> getTaskResult() {
		return taskResult;
	}
}
