package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.task.SelectedItem;
import tr.com.milia.resurgence.task.Task;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class PlanResponse {
	private final String leader;
	private final MultiPlayerTask task;
	private final List<Member> members;

	public PlanResponse(String leader, Plan plan) {
		this.leader = leader;
		task = plan.getTask();
		members = plan.getMembers().stream().map(Member::new).collect(Collectors.toList());
	}

	public String getLeader() {
		return leader;
	}

	public MultiPlayerTask getTask() {
		return task;
	}

	public List<Member> getMembers() {
		return members;
	}

	static class Member {
		private final MultiPlayerTask.Position position;
		private final Task task;
		private final String name;
		private final Plan.Member.Status status;
		private final Set<SelectedItem> selectedItems;

		public Member(Plan.Member member) {
			this.position = member.getPosition();
			this.task = member.getTask();
			this.name = member.getName();
			this.status = member.getStatus();
			this.selectedItems = member.getSelectedItems();
		}

		public MultiPlayerTask.Position getPosition() {
			return position;
		}

		public Task getTask() {
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
