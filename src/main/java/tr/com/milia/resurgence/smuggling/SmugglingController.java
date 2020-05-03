package tr.com.milia.resurgence.smuggling;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.Task;
import tr.com.milia.resurgence.task.TaskResult;

import java.util.List;

@RestController
@RequestMapping("/smuggling")
public class SmugglingController {

	private final SmugglingService service;

	public SmugglingController(SmugglingService service) {
		this.service = service;
	}

	@PostMapping("/{task}/{amount}")
	public SmugglingTaskResultResponse perform(@PathVariable("task") Task task,
											   @PathVariable("amount") int amount,
											   TokenAuthentication authentication) {
		String playerName = authentication.getPlayerName();
		List<TaskResult> results = service.perform(task, playerName, amount);

		long successCount = results.stream().filter(TaskResult::isSucceed).count();
		return new SmugglingTaskResultResponse(successCount, amount - successCount);
	}


}
