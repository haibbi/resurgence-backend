package tr.com.milia.resurgence.chat;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class Topic {

	public static final String P2P_TOPIC_PREFIX = "p2p";
	public static final String GROUP_TOPIC_PREFIX = "grp";

	private final String name;
	private final Set<Subscription> subscriptions = new HashSet<>();
	private final ConcurrentLinkedDeque<Message> messages = new ConcurrentLinkedDeque<>();
	private final AtomicLong sequenceGenerator = new AtomicLong(0);

	private Topic(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public Topic(String name, Set<Subscription> subscriptions, List<Message> messages) {
		this(name);
		this.subscriptions.addAll(subscriptions);
		messages.sort(Message::compareTo);
		this.messages.addAll(messages);
		long lastSequence = 0;
		try {
			lastSequence = this.messages.getLast().getSequence();
		} catch (NoSuchElementException ignored) {
			// ignored
		}
		this.sequenceGenerator.set(lastSequence);
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
		subscriptions.add(new Subscription(subscription));
	}

	void unsubscribe(String subscription) {
		subscriptions.remove(new Subscription(subscription));
	}

	boolean hasSubscription(String subscription) {
		return subscriptions.contains(new Subscription(subscription));
	}

	Message generateMessage(String from, String text) {
		Message message = new Message(this.sequenceGenerator.incrementAndGet(), text, from);
		this.messages.addLast(message);
		return message;
	}

	String subscriptionName(String user) {
		if (name.startsWith(GROUP_TOPIC_PREFIX)) return name.substring(GROUP_TOPIC_PREFIX.length());
		if (name.startsWith(P2P_TOPIC_PREFIX) && subscriptions.size() == 2) {
			for (Subscription subscription : subscriptions) {
				if (!subscription.getName().equals(user)) return subscription.getName();
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

	public Set<Subscription> getSubscriptions() {
		return Collections.unmodifiableSet(subscriptions);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Topic topic = (Topic) o;
		return name.equals(topic.name) &&
			subscriptions.equals(topic.subscriptions) &&
			messages.equals(topic.messages) &&
			sequenceGenerator.equals(topic.sequenceGenerator);
	}

	@Override
	public int hashCode() {
		int messagesHashCode = Arrays.hashCode(messages.toArray());
		return Objects.hash(name, subscriptions, sequenceGenerator.get(), messagesHashCode);
	}

	public void drainMessages() {
		while (this.messages.size() > 24) {
			this.messages.removeFirst();
		}
	}

	public boolean isGroup() {
		return name.startsWith(GROUP_TOPIC_PREFIX);
	}


	boolean unread(String user) {
		return sequenceGenerator.get() > subscriptions.stream()
			.filter(s -> s.getName().equals(user))
			.map(Subscription::getRead)
			.findFirst()
			.orElse(0L);
	}

	public void read(String user) {
		subscriptions.stream()
			.filter(s -> s.getName().equals(user))
			.forEach(s -> s.setRead(sequenceGenerator.get()));
	}
}
