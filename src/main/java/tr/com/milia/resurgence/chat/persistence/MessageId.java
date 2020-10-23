package tr.com.milia.resurgence.chat.persistence;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MessageId implements Serializable {

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Topic topic;

	private long sequence;

	public MessageId() {
	}

	public MessageId(Topic topic, long sequence) {
		this.topic = Objects.requireNonNull(topic);
		this.sequence = sequence;
	}

	public long getSequence() {
		return sequence;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MessageId messageId = (MessageId) o;
		return sequence == messageId.sequence &&
			topic.equals(messageId.topic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(topic, sequence);
	}
}
