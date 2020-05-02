package tr.com.milia.resurgence.chat;

import java.util.UUID;

public class Message {

	private final UUID id;
	private String content;
	private Type type;

	public Message(String content) {
		this(content, Type.MESSAGE);
	}

	public Message(Type type) {
		this(null, type);
	}

	public Message(String content, Type type) {
		id = UUID.randomUUID();
		this.type = type;
		setContent(content);
	}

	public UUID getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
