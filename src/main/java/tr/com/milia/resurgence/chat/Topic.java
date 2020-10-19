package tr.com.milia.resurgence.chat;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class Topic {

	public static final String P2P_TOPIC_PREFIX = "p2p";
	public static final String GROUP_TOPIC_PREFIX = "grp";

	private final String name;
	private final Set<String> subscriptions = new HashSet<>();
	private final ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
	private final AtomicLong sequenceGenerator = new AtomicLong(0);

	private Topic(String name) {
		this.name = Objects.requireNonNull(name);
	}

	Topic(String name, Set<String> subscriptions, List<Message> messages, long lastSeq) {
		this(name);
		this.subscriptions.addAll(subscriptions);
		messages.sort(Message::compareTo);
		this.messages.addAll(messages);
		this.sequenceGenerator.set(lastSeq);
	}

	static Topic p2p(String peer1, String peer2) {
		return new Topic(p2pName(peer1, peer2));
	}

	static String p2pName(String peer1, String peer2) {
		final String concat;
		int compare = peer1.compareTo(peer2);

		if (compare == 0) throw new IllegalArgumentException("Same peer can not create topic");

		if (compare < 0) {
			concat = length(peer1) + peer1 + peer2 + length(peer2);
		} else {
			concat = length(peer2) + peer2 + peer1 + length(peer1);
		}

		String base64 = Base64.getEncoder().encodeToString(concat.getBytes());
		return P2P_TOPIC_PREFIX + base64;
	}

	static Topic group(String name) {
		return new Topic(GROUP_TOPIC_PREFIX + name);
	}

	private static int length(String value) {
		if (value == null || value.isBlank()) return 0;
		return value.getBytes().length;
	}

	void subscribe(String subscription) {
		subscriptions.add(subscription);
	}

	void unsubscribe(String subscription) {
		subscriptions.remove(subscription);
	}

	boolean hasSubscription(String subscription) {
		return subscriptions.contains(subscription);
	}

	Message generateMessage(String from, String text) {
		Message message = new Message(this.sequenceGenerator.incrementAndGet(), text, from);
		this.messages.add(message);
		return message;
	}

	String subscriptionName(String user) {
		if (name.startsWith(GROUP_TOPIC_PREFIX)) return name.substring(GROUP_TOPIC_PREFIX.length());
		if (name.startsWith(P2P_TOPIC_PREFIX) && subscriptions.size() == 2) {
			for (String subscription : subscriptions) {
				if (!subscription.equals(user)) return subscription;
			}
		}
		return name;
	}

	public Queue<Message> getMessages() {
		return messages;
	}

	public String getName() {
		return name;
	}

	public Set<String> getSubscriptions() {
		return Collections.unmodifiableSet(subscriptions);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Topic topic = (Topic) o;
		return name.equals(topic.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
