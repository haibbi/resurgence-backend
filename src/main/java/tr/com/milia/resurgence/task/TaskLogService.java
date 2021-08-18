package tr.com.milia.resurgence.task;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.task.multiplayer.MultiPlayerTask;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

	public Duration leftTime(Player player, MultiPlayerTask task) {
		Set<Task> tasks = task.positions().stream().map(task::task).collect(Collectors.toSet());
		return repository.findFirstByTaskInAndCreatedByAndCreatedDateAfterOrderByCreatedDateDesc(
			tasks, player, Instant.now().minus(task.duration())
		).map(TaskLog::durationToLeft).orElse(Duration.ZERO);
	}

	public List<TaskLog> allPerformedTaskSince(Player player, Instant time) {
		return this.repository.findAllByCreatedByAndCreatedDateAfter(player, time);
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
