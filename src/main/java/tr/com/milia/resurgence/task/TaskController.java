package tr.com.milia.resurgence.task;

import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

	private final TaskService service;
	private final TaskLogService taskLogService;
	private final PlayerService playerService;

	public TaskController(TaskService service, TaskLogService taskLogService, PlayerService playerService) {
		this.service = service;
		this.taskLogService = taskLogService;
		this.playerService = playerService;
	}

	@PostMapping("/{task}")
	public TaskResultResponse perform(TokenAuthentication authentication,
									  @PathVariable("task") Task task,
									  @RequestBody TaskRequest request) {
		if (!task.isSolo()) throw new SoloTaskException();

		String playerName = authentication.getPlayerName();
		Map<Item, Long> selectedItems = request.selectedItems.stream()
			.collect(Collectors.toMap(SelectedItem::getItem, SelectedItem::getQuantity));
		TaskResult result = service.perform(task, playerName, selectedItems);
		return new TaskResultResponse(result);
	}

	@GetMapping
	public List<TaskResponse> tasks(TokenAuthentication authentication) {
		var player = playerService.findByName(authentication.getPlayerName()).orElseThrow(PlayerNotFound::new);

		return Arrays.stream(Task.values())
			.filter(Task::isSolo)
			.map(task -> {
				Duration duration = taskLogService.leftTime(player, task).orElse(Duration.ZERO);
				return new TaskResponse(task, duration);
			}).collect(Collectors.toList());
	}

}
