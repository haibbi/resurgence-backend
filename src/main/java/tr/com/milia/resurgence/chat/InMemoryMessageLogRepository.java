package tr.com.milia.resurgence.chat;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryMessageLogRepository {

	private static final List<MessageLog> LOGS = Collections.synchronizedList(new LinkedList<>());

	public void add(MessageLog log) {
		LOGS.add(log);
	}

	public List<MessageLog> findAllByTopic(String topic) {
		return LOGS.stream()
			.filter(log -> log.getTopic().equals(topic))
			.collect(Collectors.toList());
	}

}
