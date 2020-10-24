package tr.com.milia.resurgence.chat.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("topicMessageRepository")
public interface MessageRepository extends JpaRepository<Message, MessageId> {

	Page<Message> findAllById_Topic_Name(String topic, Pageable pageable);

}
