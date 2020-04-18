package tr.com.milia.resurgence.task;

public abstract class TaskResult {

	public boolean isSucceed() {
		return this instanceof TaskSucceedResult;
	}

}
