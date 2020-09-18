package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocalizedException;

import java.time.Duration;

public class TaskCoolDownException extends LocalizedException {

	private final Duration duration;

	public TaskCoolDownException(Duration duration) {
		super("task.cool.down");
		this.duration = duration;
	}

	private TaskCoolDownException(String code, Duration duration) {
		super(code);
		this.duration = duration;
	}

	public static TaskCoolDownException multiplayer(Duration leftTime) {
		return new TaskCoolDownException("task.cool.down.multiplayer", leftTime);
	}

	public Duration getDuration() {
		return duration;
	}

	public long getMillis() {
		return duration.toMillis();
	}
}
