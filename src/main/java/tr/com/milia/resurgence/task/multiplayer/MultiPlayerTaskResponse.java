package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.i18n.LocaleEnum;
import tr.com.milia.resurgence.task.TaskResponse;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

class MultiPlayerTaskResponse implements LocaleEnum {

	private final MultiPlayerTask task;
	private final Set<PositionResponse> positions;

	public MultiPlayerTaskResponse(MultiPlayerTask task, Duration left) {
		this.task = task;
		this.positions = task.positions().stream()
			.map(p -> new PositionResponse(task, p, left))
			.collect(Collectors.toSet());
	}

	public Set<PositionResponse> getPositions() {
		return positions;
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

	static class PositionResponse implements LocaleEnum {
		private final MultiPlayerTask.Position position;
		private final TaskResponse task;
		private final int quorum;
		private final boolean leader;

		public PositionResponse(MultiPlayerTask task, MultiPlayerTask.Position position, Duration left) {
			this.position = position;
			this.task = new TaskResponse(task.task(position), left);
			this.quorum = task.quorum(position);
			this.leader = position.isLeader();
		}

		public TaskResponse getTask() {
			return task;
		}

		public int getQuorum() {
			return quorum;
		}

		public boolean isLeader() {
			return leader;
		}

		@Override
		public String[] getCodes() {
			return position.getCodes();
		}

		@Override
		public Object[] getArguments() {
			return position.getArguments();
		}

		@Override
		public String getDefaultMessage() {
			return position.getDefaultMessage();
		}

		@Override
		public String name() {
			return position.name();
		}
	}
}
