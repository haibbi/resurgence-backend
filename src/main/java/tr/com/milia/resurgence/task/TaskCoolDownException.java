package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocalizedException;

import java.time.Duration;

public class TaskCoolDownException extends LocalizedException {

	private final Duration duration;

	public TaskCoolDownException(Duration duration) {
		super("task.cool.down");
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}
}
