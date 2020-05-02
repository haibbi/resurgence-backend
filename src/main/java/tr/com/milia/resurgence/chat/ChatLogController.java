package tr.com.milia.resurgence.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/chat/log")
public class ChatLogController {

	private final MessageLogRepository repository;

	public ChatLogController(MessageLogRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/{topic}")
	public List<MessageLog> logs(@PathVariable("topic") String topic) {
		topic = topic.replaceAll("\\$", "\\/");
		Instant duration = Instant.now().minus(Duration.ofHours(1));
		return repository.findAllByTopicAndTimeAfterOrderByTimeDesc(topic, duration);
	}

}
