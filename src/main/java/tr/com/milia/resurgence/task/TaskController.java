package tr.com.milia.resurgence.task;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;

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
		if (!task.isPerformSolo()) throw new SoloTaskException();

		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		TaskResult result = service.perform(task, playerName, request.selectedItems);
		return new TaskResultResponse(result);
	}

}
