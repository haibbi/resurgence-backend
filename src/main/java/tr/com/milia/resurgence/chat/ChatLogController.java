package tr.com.milia.resurgence.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat/log")
public class ChatLogController {

	private final InMemoryMessageLogRepository repository;

	public ChatLogController(InMemoryMessageLogRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/{topic}")
	public List<MessageLog> logs(@PathVariable("topic") String topic) {
		topic = topic.replaceAll("\\$", "\\/");
		return repository.findAllByTopic(topic);
	}

}
