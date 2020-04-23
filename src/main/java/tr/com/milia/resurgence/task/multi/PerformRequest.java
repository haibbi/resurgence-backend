package tr.com.milia.resurgence.task.multi;

import javax.validation.constraints.NotNull;

public class PerformRequest {
	@NotNull
	MultiPlayerTask task;

	public void setTask(MultiPlayerTask task) {
		this.task = task;
	}
}
