package tr.com.milia.resurgence.chat.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("topicMessageRepository")
public interface MessageRepository extends JpaRepository<Message, MessageId> {
}
