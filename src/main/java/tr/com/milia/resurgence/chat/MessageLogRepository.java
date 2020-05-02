package tr.com.milia.resurgence.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageLogRepository extends JpaRepository<MessageLog, UUID> {

	List<MessageLog> findAllByTopicAndTimeAfterOrderByTimeDesc(String topic, Instant time);

}
