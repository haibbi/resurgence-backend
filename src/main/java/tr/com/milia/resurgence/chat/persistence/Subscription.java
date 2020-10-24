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

	public Subscription() {
	}

	public Subscription(Topic topic, String name) {
		this.id = new SubscriptionId(topic, name);
	}

	public SubscriptionId getId() {
		return id;
	}
}
