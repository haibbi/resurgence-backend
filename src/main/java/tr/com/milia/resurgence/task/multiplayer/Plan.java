package tr.com.milia.resurgence.task.multiplayer;

import tr.com.milia.resurgence.task.SelectedItem;
import tr.com.milia.resurgence.task.Task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static tr.com.milia.resurgence.task.multiplayer.MultiPlayerTask.Position.LEADER;
import static tr.com.milia.resurgence.task.multiplayer.Plan.Member.Status.READY;
import static tr.com.milia.resurgence.task.multiplayer.Plan.Member.Status.WAITING;

public class Plan {

	private static final Map<String, Plan> PLANS = new ConcurrentHashMap<>();

	private final MultiPlayerTask task;
	private final List<Member> members = new CopyOnWriteArrayList<>();

	public Plan(MultiPlayerTask task, String leader, Set<SelectedItem> selectedItems) {
		if (PLANS.containsKey(leader)) throw new MemberHaveAPlanException();

		Objects.requireNonNull(leader);
		this.task = Objects.requireNonNull(task);

		this.members.add(new Member(LEADER, task.task(LEADER), leader, selectedItems, READY));
		PLANS.put(leader, this);
	}

	public static Map<String, Plan> getPlans() {
		return Collections.unmodifiableMap(PLANS);
	}

	public static Optional<Plan> find(String member) {
		Optional<Plan> leader = findLeader(member);
		if (leader.isPresent()) return leader;

		return PLANS.values().stream().filter(p -> p.members.stream().anyMatch(m -> m.name.equals(member))).findAny();
	}

	public static Optional<Plan> findLeader(String leader) {
		return Optional.ofNullable(PLANS.get(leader));
	}

	protected static void clean() {
		PLANS.clear();
	}

	public synchronized void add(MultiPlayerTask.Position position, String member) {
		for (Plan plan : PLANS.values()) {
			boolean memberInPlan = plan.members.stream().anyMatch(m -> m.name.equals(member));
			if (memberInPlan) throw new MemberHaveAPlanException();
		}

		if (position.isLeader()) throw new IllegalArgumentException("Plan have a leader already.");

		int limit = task.quorum(position);
		long count = members.stream().filter(m -> m.position == position).count();

		if (count >= limit) throw new PositionLimitExceededException();

		members.add(new Member(position, task.task(position), member, null, WAITING));
	}

	public synchronized void remove(String member) {
		if (PLANS.containsKey(member)) {
			// leader leaving plan
			PLANS.remove(member);
		} else {
			members.removeIf(m -> m.name.equals(member));
		}
	}

	public void ready(String member, Set<SelectedItem> selectedItems) {
		members.stream().filter(m -> m.name.equals(member)).findAny().ifPresent(m -> m.ready(selectedItems));
	}

	protected synchronized void check() {
		if (members.stream().anyMatch(m -> m.status != READY)) {
			throw new NotAllMemberReadyException();
		}

		task.positions().forEach(position -> {
			int quorum = task.quorum(position);
			long count = members.stream().filter(m -> m.position == position).count();

			if (quorum != count) throw new QuorumException();
		});
	}

	protected void complete(String leader) {
		PLANS.remove(leader);
	}

	protected MultiPlayerTask getTask() {
		return task;
	}

	protected List<Member> getMembers() {
		return members;
	}

	public static class Member {
		private final MultiPlayerTask.Position position;
		private final Task task;
		private final String name;
		private Set<SelectedItem> selectedItems;
		private Status status;

		public Member(MultiPlayerTask.Position position,
					  Task task,
					  String name,
					  Set<SelectedItem> selectedItems,
					  Status status) {
			this.position = position;
			this.task = task;
			this.name = name;
			this.selectedItems = selectedItems;
			this.status = status;
		}

		void ready(Set<SelectedItem> selectedItems) {
			this.selectedItems = selectedItems;
			status = READY;
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

		public Status getStatus() {
			return status;
		}

		public Set<SelectedItem> getSelectedItems() {
			if (selectedItems == null) return Collections.emptySet();
			return Collections.unmodifiableSet(selectedItems);
		}

		enum Status {
			WAITING, READY
		}
	}
}
