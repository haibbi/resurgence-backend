package tr.com.milia.resurgence.chat.persistence;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Topic {

	@Id
	private String name;

	@OneToMany(
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		mappedBy = "id.topic"
	)
	private Set<Subscription> subscriptions = new HashSet<>();

	@OneToMany(
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		mappedBy = "id.topic"
	)
	private Set<Message> messages = new HashSet<>();

	public Topic() {
	}

	public Topic(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
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
