package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.task.SelectedItem;
import tr.com.milia.resurgence.task.TaskResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class PlanResponse {
	private final String leader;
	private final MultiPlayerTaskResponse task;
	private final List<Member> members;

	public PlanResponse(Plan plan) {
		this.leader = plan.leader();
		task = new MultiPlayerTaskResponse(plan.getTask(), 0);
		members = plan.getMembers().stream().map(Member::new).collect(Collectors.toList());
	}

	public String getLeader() {
		return leader;
	}

	public MultiPlayerTaskResponse getTask() {
		return task;
	}

	public List<Member> getMembers() {
		return members;
	}

	static class Member {
		private final MultiPlayerTask.Position position;
		private final TaskResponse task;
		private final String name;
		private final Plan.Member.Status status;
		private final Set<SelectedItem> selectedItems;

		public Member(Plan.Member member) {
			this.position = member.getPosition();
			this.task = new TaskResponse(member.getTask(), null);
			this.name = member.getName();
			this.status = member.getStatus();
			this.selectedItems = member.getSelectedItems();
		}

		public MultiPlayerTask.Position getPosition() {
			return position;
		}

		public TaskResponse getTask() {
			return task;
		}

		public String getName() {
			return name;
		}

		public Plan.Member.Status getStatus() {
			return status;
		}

		public Set<SelectedItem> getSelectedItems() {
			return selectedItems;
		}
	}
}
