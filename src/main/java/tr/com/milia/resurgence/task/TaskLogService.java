package tr.com.milia.resurgence.task;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.player.Player;

import java.time.Duration;
import java.util.Optional;

@Service
public class TaskLogService {

	private final TaskLogRepository repository;


	public TaskLogService(TaskLogRepository repository) {
		this.repository = repository;
	}

	public void checkPerform(Player player, Task task) {
		repository.findFirstByTaskAndCreatedByOrderByCreatedDateDesc(task, player).ifPresent(taskLog -> {
			if (!taskLog.isExpired()) throw new TaskCoolDownException(taskLog.durationToLeft());
		});
	}

	Optional<Duration> leftTime(Player player, Task task) {
		return repository.findFirstByTaskAndCreatedByOrderByCreatedDateDesc(task, player)
			.filter(taskLog -> !taskLog.isExpired())
			.map(TaskLog::durationToLeft);
	}

	@EventListener(TaskStartedEvent.class)
	public void onTaskStartedEvent(TaskStartedEvent event) {
		checkPerform(event.getPlayer(), event.getTask());
	}

	@EventListener(TaskResult.class)
	public void onTaskResult(TaskResult result) {
		repository.save(new TaskLog(result.getTask(), result.getPlayer(), result.isSucceed()));
	}
}
