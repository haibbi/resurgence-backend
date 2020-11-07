package tr.com.milia.resurgence.chat;

import java.util.Objects;

public class SubscriptionResponse {
	private final String topic;
	private final String name;
	private final boolean unread;

	public SubscriptionResponse(String topic, String name, boolean unread) {
		this.topic = Objects.requireNonNull(topic);
		this.name = Objects.requireNonNull(name);
		this.unread = unread;
	}

	public String getTopic() {
		return topic;
	}

	public String getName() {
		return name;
	}

	public boolean isUnread() {
		return unread;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SubscriptionResponse that = (SubscriptionResponse) o;
		return topic.equals(that.topic) &&
			name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(topic, name);
	}
}
