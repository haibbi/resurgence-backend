package tr.com.milia.resurgence.task.multiplayer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.task.TaskLogService;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/multiplayer-task/player")
public class MultiplayerTaskPlayerInfoController {

	private final PlayerService playerService;
	private final TaskLogService taskLogService;

	public MultiplayerTaskPlayerInfoController(PlayerService playerService, TaskLogService taskLogService) {
		this.playerService = playerService;
		this.taskLogService = taskLogService;
	}

	@GetMapping("/{player}")
	public List<MultiplayerTaskPlayerInfoResponse> playerInfo(@PathVariable("player") String playerName) {
		Player player = playerService.findByName(playerName).orElseThrow();

		return Arrays.stream(MultiPlayerTask.values())
			.map(task -> {
				final Duration leftTime = taskLogService.leftTime(player, task);
				return new MultiplayerTaskPlayerInfoResponse(task, leftTime.isZero());
			})
			.collect(Collectors.toList());
	}

}
