package tr.com.milia.resurgence.notification;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Long> {
	Optional<Message> findByIdAndTo_Name(Long id, String to);

	List<Message> findAllByTo_NameAndDeletedIsFalse(String to);
}
