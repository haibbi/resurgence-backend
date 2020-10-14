package tr.com.milia.resurgence.notification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.player.Player;

import java.util.List;

@Service
public class MessageService {

	private final MessageRepository repository;

	public MessageService(MessageRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void delete(String player, Long id) {
		repository.findByIdAndTo_Name(id, player).ifPresent(Message::delete);
	}

	public List<Message> messages(String player) {
		return repository.findAllByTo_NameAndDeletedIsFalse(player);
	}

	Message send(Player to, String title, String content) {
		return repository.save(new Message(to, title, content));
	}
}
