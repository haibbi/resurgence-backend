package tr.com.milia.resurgence.task;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/task")
public class TaskController {

	private final TaskService service;

	public TaskController(TaskService service) {
		this.service = service;
	}

	@PostMapping
	public TaskResultResponse perform(Principal principal) {
		TaskResult result = service.perform(Task.BANK_RUBBERY, principal.getName());
		return new TaskResultResponse(result);
	}

}
