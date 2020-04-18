package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.i18n.LocalizedException;

import java.time.Duration;

public class TaskCoolDownException extends LocalizedException {

	public TaskCoolDownException(Duration timeToLeft) {
		super("task.cool.down", timeToLeft);
	}

}
