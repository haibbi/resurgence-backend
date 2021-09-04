package tr.com.milia.resurgence.task;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {

	Optional<TaskLog> findFirstByTaskAndCreatedByOrderByCreatedDateDesc(Task task, Player createdBy);

	Optional<TaskLog> findFirstByTaskInAndCreatedByAndCreatedDateAfterOrderByCreatedDateDesc(
		Collection<Task> task, Player createdBy, Instant createdDate);

	List<TaskLog> findAllByCreatedByAndCreatedDateAfter(Player createdBy, Instant createdDate);

}
