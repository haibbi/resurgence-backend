package tr.com.milia.resurgence.chat.persistence;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "topic_subscriptions")
public class Subscription implements Serializable {

	@EmbeddedId
	private SubscriptionId id;

	private long sequence;

	public Subscription() {
	}

	public Subscription(Topic topic, String name, long sequence) {
		this.id = new SubscriptionId(topic, name);
		this.sequence = sequence;
	}

	public SubscriptionId getId() {
		return id;
	}

	public long getSequence() {
		return sequence;
	}
}
