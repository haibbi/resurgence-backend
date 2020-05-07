package tr.com.milia.resurgence.task;

import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

	private final TaskService service;

	public TaskController(TaskService service) {
		this.service = service;
	}

	@PostMapping("/{task}")
	public TaskResultResponse perform(TokenAuthentication authentication,
									  @PathVariable("task") Task task,
									  @RequestBody TaskRequest request) {
		if (!task.isSolo()) throw new SoloTaskException();

		String playerName = authentication.getPlayerName();
		TaskResult result = service.perform(task, playerName, request.selectedItems);
		return new TaskResultResponse(result);
	}

	@GetMapping
	public List<Task> tasks() {
		return Arrays.stream(Task.values()).filter(Task::isSolo).collect(Collectors.toList());
	}

}
