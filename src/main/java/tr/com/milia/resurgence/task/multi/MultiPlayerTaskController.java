package tr.com.milia.resurgence.task.multi;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;
import tr.com.milia.resurgence.task.TaskResult;

import java.util.Map;

@RestController
@RequestMapping("/task/multiplayer")
public class MultiPlayerTaskController {

	private final MultiPlayerTaskService service;

	public MultiPlayerTaskController(MultiPlayerTaskService service) {
		this.service = service;
	}

	@PostMapping("/invite/{category}/{playerName}")
	public void invite(TokenAuthentication authentication,
					   @PathVariable("category") MultiPlayerTask.Category category,
					   @PathVariable("playerName") String playerName) {
		String leader = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.invite(leader, playerName, category);
	}

	@DeleteMapping("/remove/{playerName}")
	public void remove(TokenAuthentication authentication, @PathVariable("playerName") String playerName) {
		String leader = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.remove(leader, playerName);
	}

	@DeleteMapping("/exit")
	public void exit(TokenAuthentication authentication) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.exit(playerName);
	}

	@PostMapping("/prepare")
	public void prepare(TokenAuthentication authentication, @RequestBody @Validated PrepareRequest request) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.prepare(playerName, request.category, request.selectedItems);
	}

	@PostMapping("/perform/{task}")
	public MultiPlayerTaskResultResponse perform(TokenAuthentication authentication, @PathVariable("task") MultiPlayerTask task) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		Map<String, TaskResult> result = service.perform(task, playerName);
		return new MultiPlayerTaskResultResponse(result);
	}

	@GetMapping("/status")
	public ResponseEntity<StatusResponse> status(TokenAuthentication authentication) {
		String playerName = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		Map<String, Status> status = service.status(playerName);
		if (status.isEmpty()) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(new StatusResponse(status));
	}

}
