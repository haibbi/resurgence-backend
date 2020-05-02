package tr.com.milia.resurgence.chat;

public class MessageLog {

	private String content;
	private String topic;
	private Type type;
	private String player;

	public MessageLog() {
	}

	public MessageLog(String content, String topic, Type type, String player) {
		this.content = content;
		this.topic = topic;
		this.type = type;
		this.player = player;
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
}
