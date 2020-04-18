package tr.com.milia.resurgence.task;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.Optional;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {

	Optional<TaskLog> findFirstByTaskAndCreatedByOrderByCreatedDateDesc(Task task, Player createdBy);

}
