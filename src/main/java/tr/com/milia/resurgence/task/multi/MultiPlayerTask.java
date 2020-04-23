package tr.com.milia.resurgence.task.multi;

import tr.com.milia.resurgence.task.Task;

import java.util.Collections;
import java.util.Map;

import static java.util.Map.of;
import static tr.com.milia.resurgence.task.Task.HEIST_DRIVER;
import static tr.com.milia.resurgence.task.Task.HEIST_LEADER;
import static tr.com.milia.resurgence.task.multi.MultiPlayerTask.Category.DRIVER;
import static tr.com.milia.resurgence.task.multi.MultiPlayerTask.Category.LEADER;

public enum MultiPlayerTask {

	HEIST(of(
		LEADER, HEIST_LEADER,
		DRIVER, HEIST_DRIVER
	), of(LEADER, 1, DRIVER, 1));

	private final Map<Category, Task> tasks;
	private final Map<Category, Integer> requiredCategory;

	MultiPlayerTask(Map<Category, Task> tasks, Map<Category, Integer> requiredCategory) {
		this.tasks = tasks;
		this.requiredCategory = requiredCategory;
	}


	public Map<Category, Task> getTasks() {
		return Collections.unmodifiableMap(tasks);
	}

	public Map<Category, Integer> getRequiredCategory() {
		return Collections.unmodifiableMap(requiredCategory);
	}

	// todo rename as Position
	public enum Category {
		LEADER,

		DRIVER,

		WEAPON_MASTER
	}

}
