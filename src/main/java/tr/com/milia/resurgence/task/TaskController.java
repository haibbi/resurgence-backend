package tr.com.milia.resurgence.task;

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

	@PostMapping
	public TaskResultResponse perform(TokenAuthentication authentication, @RequestBody TaskRequest request) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		TaskResult result = service.perform(Task.BANK_RUBBERY, playerName, request.selectedItems);
		return new TaskResultResponse(result);
	}

}
