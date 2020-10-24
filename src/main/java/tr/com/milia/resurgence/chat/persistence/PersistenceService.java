package tr.com.milia.resurgence.chat.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.chat.Topic;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Lazy(value = false)
public class PersistenceService {

	private static final Logger log = LoggerFactory.getLogger(PersistenceService.class);

	private final TopicRepository repository;
	private final MessageRepository messageRepository;

	public PersistenceService(TopicRepository repository, MessageRepository messageRepository) {
		this.repository = repository;
		this.messageRepository = messageRepository;
	}

	public void persist(Topic toSave) {
		log.debug("Topic[{}] persisting started.", toSave.getName());
		final tr.com.milia.resurgence.chat.persistence.Topic topic =
			new tr.com.milia.resurgence.chat.persistence.Topic(toSave.getName());

		final Set<Subscription> subscriptions = toSave.getSubscriptions().stream()
			.map(s -> new Subscription(topic, s))
			.collect(Collectors.toSet());

		final Set<Message> messages = toSave.getMessages().stream()
			.map(m -> new Message(topic, m.getSequence(), m.getContent(), m.getFrom(), m.getTime()))
			.collect(Collectors.toSet());

		topic.setSubscriptions(subscriptions);
		topic.setMessages(messages);

		repository.save(topic);
		log.debug("Topic[{}] persisting completed.", toSave.getName());
	}

	@Transactional
	public Collection<Topic> read() {
		return repository.findAll().stream().map(t -> {
			Set<String> subscriptions = t.getSubscriptions().stream()
				.map(Subscription::getId)
				.map(SubscriptionId::getName)
				.collect(Collectors.toSet());
			List<tr.com.milia.resurgence.chat.Message> messages = messageRepository
				.findAll(PageRequest.of(0, 24, Sort.by("id.sequence").descending()))
				.get()
				.map(m -> new tr.com.milia.resurgence.chat.Message(
					m.getSequence(), m.getContent(), m.getFrom(), m.getTime()))
				.collect(Collectors.toList());
			return new Topic(t.getName(), subscriptions, messages);
		}).collect(Collectors.toSet());
	}
}
