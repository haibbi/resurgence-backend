package tr.com.milia.resurgence.task;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.player.Player;

@Service
public class TaskLogService {

	private final TaskLogRepository repository;


	public TaskLogService(TaskLogRepository repository) {
		this.repository = repository;
	}

	void checkPerform(Player player, Task task) {
		repository.findFirstByTaskAndCreatedByOrderByCreatedDateDesc(task, player).ifPresent(taskLog -> {
			if (!taskLog.isExpired()) throw new TaskCoolDownException(taskLog.durationToLeft());
		});
	}

	@EventListener(TaskResult.class)
	public void onTaskResult(TaskResult result) {
		repository.save(new TaskLog(result.getTask(), result.getPlayer(), result.isSucceed()));
	}
}
