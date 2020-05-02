package tr.com.milia.resurgence.chat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
public class MessageLog {

	@Id
	private UUID id;
	private String content;
	private String topic;
	@Enumerated(EnumType.STRING)
	private Type type;
	private String player;
	private Instant time;

	public MessageLog() {
	}

	public MessageLog(Message message, String topic, String player) {
		id = message.getId();
		content = message.getContent();
		type = message.getType();
		this.topic = topic;
		this.player = player;
		time = Instant.now();
	}

	public UUID getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getTopic() {
		return topic;
	}

	public Type getType() {
		return type;
	}

	public String getPlayer() {
		return player;
	}

	public Instant getTime() {
		return time;
	}
}
