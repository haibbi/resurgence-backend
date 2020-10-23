package tr.com.milia.resurgence.chat.persistence;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Entity(name = "TopicMessage")
@Table(name = "topic_messages")
public class Message {

	@EmbeddedId
	private MessageId id;

	private String content;

	@Column(name = "_from")
	private String from;

	private Instant time;

	public Message() {
	}

	public Message(Topic topic, long sequence, String content, String from, Instant time) {
		this.id = new MessageId(topic, sequence);
		this.content = content;
		this.from = from;
		this.time = time;
	}

	public long getSequence() {
		return id.getSequence();
	}

	public String getContent() {
		return content;
	}

	public String getFrom() {
		return from;
	}

	public Instant getTime() {
		return time;
	}
}
