package tr.com.milia.resurgence.task.multiplayer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.TaskLogService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/multiplayer-task")
public class MultiplayerTaskController {

	private final PlayerService playerService;
	private final TaskLogService taskLogService;

	public MultiplayerTaskController(PlayerService playerService, TaskLogService taskLogService) {
		this.playerService = playerService;
		this.taskLogService = taskLogService;
	}

	@GetMapping
	public List<MultiPlayerTaskResponse> all(TokenAuthentication authentication) {
		Player player = playerService.findByName(authentication.getPlayerName()).orElseThrow();

		return Arrays.stream(MultiPlayerTask.values()).map(task -> {
			long leftTime = taskLogService.leftTime(player, task).toMillis();
			return new MultiPlayerTaskResponse(task, leftTime);
		}).collect(Collectors.toList());
	}

}
