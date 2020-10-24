package tr.com.milia.resurgence.chat.persistence;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubscriptionId implements Serializable {

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Topic topic;

	private String name;

	public SubscriptionId() {
	}

	public SubscriptionId(Topic topic, String name) {
		this.topic = Objects.requireNonNull(topic);
		this.name = Objects.requireNonNull(name);
	}

	public Topic getTopic() {
		return topic;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SubscriptionId that = (SubscriptionId) o;
		return topic.equals(that.topic) &&
			name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(topic, name);
	}
}
