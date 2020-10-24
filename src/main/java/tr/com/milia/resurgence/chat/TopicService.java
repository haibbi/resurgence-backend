package tr.com.milia.resurgence.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import tr.com.milia.resurgence.chat.persistence.PersistenceService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;
import tr.com.milia.resurgence.security.TokenAuthentication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.security.Principal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TopicService {

	private static final Logger log = LoggerFactory.getLogger(TopicService.class);

	private static final String PERSISTING_PERIOD = "PT15M";
	private static final String ONLINE_USERS_DESTINATION = "/online-players";
	private static final String PLAYERS_DESTINATION = "/players";
	private static final String SUBSCRIPTION_DESTINATION = "/subscriptions";

	private static final Set<Topic> DEFAULT_TOPICS = Set.of(Topic.group("general"));
	private static final Map<String, Topic> TOPICS = new ConcurrentHashMap<>();
	private static int TOPICS_HASH_CODE;

	private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

	private final SimpMessagingTemplate template;
	private final PlayerService playerService;
	private final PersistenceService persistenceService;

	public TopicService(SimpMessagingTemplate template, PlayerService playerService, PersistenceService persistenceService) {
		this.template = template;
		this.playerService = playerService;
		this.persistenceService = persistenceService;
	}

	@PostConstruct
	public void init() {
		persistenceService.read().forEach(t -> TOPICS.put(t.getName(), t));

		for (Topic topic : DEFAULT_TOPICS) {
			if (!TOPICS.containsKey(topic.getName())) {
				TOPICS.put(topic.getName(), topic);
			}
		}

		TOPICS_HASH_CODE = TOPICS.hashCode();
	}

	@PreDestroy
	public void destroy() {
		persist();
	}

	Collection<Message> messages(String user, String topicName) {
		Objects.requireNonNull(user);
		Objects.requireNonNull(topicName);
		Topic topic = TOPICS.get(topicName);

		if (topic == null) return Collections.emptyList();
		if (!topic.hasSubscription(user)) return Collections.emptyList();

		return topic.getMessages();
	}

	Collection<Subscription> subscriptions(String user) {
		Objects.requireNonNull(user);
		return TOPICS.values().stream()
			.filter(t -> t.hasSubscription(user))
			.map(t -> new Subscription(t.getName(), t.subscriptionName(user)))
			.collect(Collectors.toList());
	}

	void createP2PTopic(String from, String to) {
		Objects.requireNonNull(from);
		Objects.requireNonNull(to);

		final String topicName = Topic.p2pName(from, to);
		Topic topic = TOPICS.get(topicName);

		if (topic == null) {
			topic = Topic.p2p(from, to);
			topic.subscribe(from);
			topic.subscribe(to);
			TOPICS.put(topicName, topic);
			sendSubscriptions(to);
		}
		sendSubscriptions(from);
	}

	void sendSubscriptions(String user) {
		Collection<Subscription> subscriptions = subscriptions(user);
		template.convertAndSendToUser(user, SUBSCRIPTION_DESTINATION, subscriptions);
	}

	public void sendMessage(String playerName, String topicName, String text) {
		Topic topic = TOPICS.get(topicName);
		if (topic == null) return;
		if (!topic.hasSubscription(playerName)) return;

		Message message = topic.generateMessage(playerName, text);
		topic.getSubscriptions().forEach(s -> sendMessageInternal(s, topicName, message));
	}

	public void sendMessages(String playerName, final String topicName) {
		messages(playerName, topicName).forEach(m -> sendMessageInternal(playerName, topicName, m));
	}

	private void sendMessageInternal(String user, String topic, Message message) {
		Objects.requireNonNull(user);
		Objects.requireNonNull(topic);
		Objects.requireNonNull(message);

		if (!topic.startsWith("/")) topic = "/" + topic;
		template.convertAndSendToUser(user, topic, message);
	}

	public void sendOnlineUsers(String user) {
		template.convertAndSendToUser(user, ONLINE_USERS_DESTINATION, onlineUsers);
	}

	public void sendOnlineUsers() {
		onlineUsers.forEach(user -> template.convertAndSendToUser(user, ONLINE_USERS_DESTINATION, onlineUsers));
	}

	@EventListener(SessionConnectedEvent.class)
	public void on(SessionConnectedEvent event) {
		Principal user = event.getUser();
		if (user instanceof TokenAuthentication) {
			TokenAuthentication authentication = (TokenAuthentication) user;
			String playerName = authentication.getPlayerName();
			onlineUsers.add(playerName);
			DEFAULT_TOPICS.forEach(t -> t.subscribe(playerName));
			schedule(this::sendOnlineUsers, Duration.ofSeconds(1));
		}
	}

	@EventListener(SessionDisconnectEvent.class)
	public void on(SessionDisconnectEvent event) {
		Principal user = event.getUser();
		if (user instanceof TokenAuthentication) {
			TokenAuthentication authentication = (TokenAuthentication) user;
			String playerName = authentication.getPlayerName();
			onlineUsers.remove(playerName);
			schedule(this::sendOnlineUsers, Duration.ofSeconds(1));
		}
	}

	@EventListener(SessionSubscribeEvent.class)
	public void on(SessionSubscribeEvent event) {
		Principal user = event.getUser();
		if (!(user instanceof TokenAuthentication)) return;
		final TokenAuthentication authentication = (TokenAuthentication) user;
		final String playerName = authentication.getPlayerName();

		final Object topicObject = event.getMessage().getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER);
		if (!(topicObject instanceof String)) return;

		final String topic = (String) topicObject;

		if (Pattern.matches("/user/.*/(grp|p2p).*", topic)) {
			String topicName = topic.replaceFirst("/user/.*/", "");
			schedule(() -> sendMessages(playerName, topicName), Duration.ofSeconds(1));
			schedule(() -> sendMessages(playerName, topicName), Duration.ofSeconds(3));
		}

		if (Pattern.matches("/user/.*/online-players", topic)) {
			schedule(() -> sendOnlineUsers(playerName), Duration.ofSeconds(1));
			schedule(() -> sendOnlineUsers(playerName), Duration.ofSeconds(3));
		}
	}

	public void filterAndSendPlayer(String currentPlayer, String playerName) {
		List<Subscription> playerNames = playerService.filterByName(playerName).stream()
			.map(Player::getName)
			.filter(pn -> !currentPlayer.equals(pn))
			.map(p -> new Subscription(Topic.p2pName(currentPlayer, p), p))
			.collect(Collectors.toList());
		template.convertAndSendToUser(currentPlayer, PLAYERS_DESTINATION, playerNames);
	}

	void schedule(Runnable command, Duration duration) {
		executor.schedule(command, duration.toMillis(), TimeUnit.MILLISECONDS);
	}

	@Scheduled(initialDelayString = PERSISTING_PERIOD, fixedRateString = PERSISTING_PERIOD)
	private void persist() {
		int hashCode = TOPICS.hashCode();
		if (hashCode == TOPICS_HASH_CODE) {
			log.debug("No need to persist topics. Hash code is same");
			return;
		}
		TOPICS_HASH_CODE = hashCode;
		TOPICS.values().forEach(persistenceService::persist);
	}
}
