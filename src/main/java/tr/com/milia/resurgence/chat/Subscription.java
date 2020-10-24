package tr.com.milia.resurgence.chat;

import java.util.Objects;

public class Subscription {
	private final String topic;
	private final String name;

	public Subscription(String topic, String name) {
		this.topic = Objects.requireNonNull(topic);
		this.name = Objects.requireNonNull(name);
	}

	public String getTopic() {
		return topic;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Subscription that = (Subscription) o;
		return topic.equals(that.topic) &&
			name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(topic, name);
	}
}
