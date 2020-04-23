package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.task.Task;

import java.util.Collections;
import java.util.Map;

import static java.util.Map.of;
import static tr.com.milia.resurgence.task.Task.HEIST_DRIVER;
import static tr.com.milia.resurgence.task.Task.HEIST_LEADER;
import static tr.com.milia.resurgence.task.multi.MultiPlayerTask.Position.DRIVER;
import static tr.com.milia.resurgence.task.multi.MultiPlayerTask.Position.LEADER;

public enum MultiPlayerTask {

	HEIST(of(
		LEADER, HEIST_LEADER,
		DRIVER, HEIST_DRIVER
	), of(LEADER, 1, DRIVER, 1));

	private final Map<Position, Task> tasks;
	private final Map<Position, Integer> quorum;

	MultiPlayerTask(Map<Position, Task> tasks, Map<Position, Integer> quorum) {
		this.tasks = tasks;
		this.quorum = quorum;
	}


	public Map<Position, Task> getTasks() {
		return Collections.unmodifiableMap(tasks);
	}

	public Map<Position, Integer> getQuorum() {
		return Collections.unmodifiableMap(quorum);
	}

	public enum Position {
		LEADER,

		DRIVER,

		WEAPON_MASTER
	}

}
