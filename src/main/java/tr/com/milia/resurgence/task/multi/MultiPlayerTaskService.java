package tr.com.milia.resurgence.task.multi;

import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.PlayerNotFound;
import tr.com.milia.resurgence.task.Task;
import tr.com.milia.resurgence.task.TaskResult;
import tr.com.milia.resurgence.task.TaskService;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static tr.com.milia.resurgence.task.multi.MultiPlayerTask.Position.LEADER;

@Service
public class MultiPlayerTaskService {

	private final static Map<String, Preparation> PREPARATION = new ConcurrentHashMap<>();
	private final static Map<String, Invitation> INVITATION = new ConcurrentHashMap<>();

	private final TaskService taskService;
	private final PlayerService playerService;

	public MultiPlayerTaskService(TaskService taskService, PlayerService playerService) {
		this.taskService = taskService;
		this.playerService = playerService;
	}

	public void invite(String leader, String playerName, MultiPlayerTask.Position position) {
		if (INVITATION.containsKey(playerName))
			throw new RuntimeException("Player already invited another multiple task");
		if (!PREPARATION.containsKey(leader))
			throw new RuntimeException("Leader does not ready to invite other people");
		if (position == LEADER)
			throw new RuntimeException("Multiple task must have only one leader");
		if (playerService.findByName(playerName).isEmpty()) throw new PlayerNotFound();

		INVITATION.put(playerName, new Invitation(leader, position));
	}

	public Map<String, Status> status(String playerName) {
		final String leader = Optional.ofNullable(INVITATION.get(playerName)).map(Invitation::getLeader).orElse(
			Optional.ofNullable(PREPARATION.get(playerName)).map(Preparation::getLeader).orElse(null)
		);

		if (leader == null) return Collections.emptyMap();

		Map<String, Status> result = new HashMap<>();
		INVITATION.entrySet().stream()
			.filter(e -> e.getValue().leader.equals(leader))
			.forEach(e -> {
				String player = e.getKey();
				Invitation invitation = e.getValue();
				result.put(player, new Status(invitation.position, null, true, false));
			});

		PREPARATION.entrySet().stream()
			.filter(e -> e.getValue().leader.equals(leader))
			.forEach(e -> {
				String player = e.getKey();
				Preparation preparation = e.getValue();
				if (result.containsKey(player)) {
					result.put(player, new Status(preparation.position,
						preparation.selectedItems,
						true,
						true));
				} else {
					result.put(player, new Status(preparation.position,
						preparation.selectedItems,
						false,
						true));
				}
			});

		return result;
	}

	public void remove(String leader, String playerName) {
		if (INVITATION.containsKey(playerName) && leader.equals(INVITATION.get(playerName).leader))
			INVITATION.remove(playerName);
		if (PREPARATION.containsKey(playerName) && leader.equals(PREPARATION.get(playerName).leader))
			PREPARATION.remove(playerName);
	}

	public void exit(String playerName) {
		Invitation invitation = INVITATION.remove(playerName);
		if (invitation != null && invitation.position == LEADER) {
			INVITATION.entrySet().removeIf(e -> playerName.equals(e.getValue().leader));
		}
		Preparation preparation = PREPARATION.remove(playerName);
		if (preparation != null && preparation.position == LEADER) {
			PREPARATION.entrySet().removeIf(e -> playerName.equals(e.getValue().leader));
		}
	}

	public void prepare(String playerName, MultiPlayerTask.Position position, Map<Item, Integer> selectedItems) {
		final String leader;
		if (position != LEADER) {
			Invitation invitation = INVITATION.get(playerName);
			if (invitation == null) throw new RuntimeException("You are not invite any multiple task");
			leader = invitation.leader;
		} else {
			if (INVITATION.containsKey(playerName))
				throw new RuntimeException("You are already invited a multiplayer task. You cannot leader!");
			leader = playerName;
		}
		if (PREPARATION.containsKey(playerName) && PREPARATION.get(playerName).position != position) {
			throw new RuntimeException("You cannot change your position");
		}
		PREPARATION.put(playerName, new Preparation(leader, position, selectedItems));
	}

	@Transactional
	public Map<String, TaskResult> perform(MultiPlayerTask task, String leader) {
		if (!PREPARATION.containsKey(leader)) {
			throw new RuntimeException("You are not prepared to perform multiplayer task");
		}
		if (!PREPARATION.get(leader).leader.equals(leader)) {
			throw new RuntimeException("You are not leader! Only leader can perform multiplayer task!");
		}

		var members = PREPARATION.entrySet().stream()
			.filter(e -> leader.equals(e.getValue().leader))
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());

		members.forEach(player -> {
			if (!PREPARATION.containsKey(player))
				throw new RuntimeException("Members does not prepared for multiplayer task");
		});

		task.getQuorum().forEach((position, count) -> {
			var categories = members.stream()
				.map(PREPARATION::get)
				.map(Preparation::getPosition)
				.collect(Collectors.toList());

			if (count != Collections.frequency(categories, position))
				throw new RuntimeException("ekibini düzgün topla gel");
		});

		Map<String, TaskResult> results = members.stream().map(player -> {
			Preparation preparation = PREPARATION.get(player);
			Task singleTask = task.getTasks().get(preparation.position);
			TaskResult result = taskService.perform(singleTask, player, preparation.selectedItems);
			return Map.entry(player, result);
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		members.forEach(PREPARATION::remove); // todo remove all by leader !!!
		members.forEach(INVITATION::remove);

		return results;
	}

}
