package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.task.TaskResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class MultiPlayerTaskResponse implements LocaleEnum {

	private final MultiPlayerTask task;
	private final Set<MultiPlayerTask.Position> positions;
	/**
	 * Left seconds
	 */
	private final long left;

	public MultiPlayerTaskResponse(MultiPlayerTask task, long left) {
		this.task = task;
		this.positions = task.positions();
		this.left = left;
	}

	public List<MultiPlayerTask.Position> getPositions() {
		// todo refactor
		List<MultiPlayerTask.Position> result = new ArrayList<>();
		positions.forEach(position -> IntStream.range(0, task.quorum(position)).forEach(i -> result.add(position)));
		return result;
	}

	public long getLeft() {
		return left;
	}

	public TaskResponse getLeaderTask() {
		return new TaskResponse(task.task(MultiPlayerTask.Position.LEADER));
	}

	@Override
	public String[] getCodes() {
		return task.getCodes();
	}

	@Override
	public Object[] getArguments() {
		return task.getArguments();
	}

	@Override
	public String getDefaultMessage() {
		return task.getDefaultMessage();
	}

	@Override
	public String name() {
		return task.name();
	}
}
