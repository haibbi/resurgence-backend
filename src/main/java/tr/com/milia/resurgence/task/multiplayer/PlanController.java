package tr.com.milia.resurgence.task.multiplayer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.item.PlayerItemService;
import tr.com.milia.resurgence.item.RequiredItemException;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan")
public class PlanController {

	private final PlayerService playerService;
	private final TaskService taskService;
	private final PlayerItemService playerItemService;
	private final TaskLogService taskLogService;

	public PlanController(PlayerService playerService,
						  TaskService taskService,
						  PlayerItemService playerItemService,
						  TaskLogService taskLogService) {
		this.playerService = playerService;
		this.taskService = taskService;
		this.playerItemService = playerItemService;
		this.taskLogService = taskLogService;
	}

	@PostMapping("/{task}")
	public PlanResponse create(
		TokenAuthentication authentication,
		@PathVariable("task") MultiPlayerTask task,
		@RequestBody @Valid MultiPlayerTaskRequest request
	) {
		Player leader = findPlayer(authentication.getPlayerName());
		Plan plan = new Plan(task, leader.getName(), request.selectedItems);
		return new PlanResponse(leader.getName(), plan);
	}

	@PostMapping("/add/{position}/{member}")
	public void add(
		TokenAuthentication authentication,
		@PathVariable("position") MultiPlayerTask.Position position,
		@PathVariable("member") String memberName
	) {
		Plan plan = Plan.findLeader(authentication.getPlayerName()).orElseThrow();
		Player member = findPlayer(memberName);
		plan.add(position, member.getName());
	}

	@PostMapping
	public ResponseEntity<List<AbstractMultiplayerTaskResultResponse>> execute(TokenAuthentication authentication) {
		String leader = authentication.getPlayerName();
		Plan plan = Plan.findLeader(leader).orElseThrow();
		plan.check();

		// first check player have item and cool down
		List<AbstractMultiplayerTaskResultResponse> results = new ArrayList<>();
		for (Plan.Member member : plan.getMembers()) {
			Player player = findPlayer(member.getName());
			try {
				playerItemService.checkItem(player, member.getTask(), member.getSelectedItems());
			} catch (RequiredItemException e) {
				results.add(new MultiplayerTaskFailedResultResponse(player.getName(), e.getCategory()));
			}

			try {
				taskLogService.checkPerform(player, member.getTask());
			} catch (TaskCoolDownException e) {
				results.add(new MemberCoolDownResponse(player.getName(), e.getDuration()));
			}
		}
		if (!results.isEmpty()) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(results);

		// Checks passes. Let's so some action.
		for (Plan.Member member : plan.getMembers()) {
			Map<Item, Long> selectedItemMap = member.getSelectedItems()
				.stream()
				.collect(Collectors.toMap(SelectedItem::getItem, SelectedItem::getQuantity));

			TaskResult result = taskService.perform(member.getTask(), member.getName(), selectedItemMap);
			results.add(new MultiplayerTaskSucceededResultResponse(member.getName(), result));
		}

		plan.remove(leader);

		return ResponseEntity.ok(results);
	}

	@DeleteMapping({"", "/", "/{member}"})
	public void remove(
		TokenAuthentication authentication,
		@PathVariable(value = "member", required = false) String memberName
	) {
		if (memberName == null) {
			memberName = authentication.getPlayerName();
		}
		Plan plan = Plan.findLeader(authentication.getPlayerName()).orElseThrow();
		plan.remove(memberName);
	}

	@PatchMapping("/ready")
	public void ready(TokenAuthentication authentication, @RequestBody MultiPlayerTaskRequest request) {
		Plan.find(authentication.getPlayerName())
			.ifPresent(plan -> plan.ready(authentication.getPlayerName(), request.selectedItems));
	}

	@GetMapping
	public ResponseEntity<PlanResponse> active(TokenAuthentication authentication) {
		Player leader = findPlayer(authentication.getPlayerName());
		return Plan.find(leader.getName())
			.map(p -> new PlanResponse(leader.getName(), p))
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/all")
	public List<PlanResponse> all() {
		return Plan.getPlans().entrySet().stream()
			.map(e -> new PlanResponse(e.getKey(), e.getValue()))
			.collect(Collectors.toList());
	}

	private Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}
}
