package tr.com.milia.resurgence.chat;

public class ChatMessageEvent {

	private final String from;
	private final String to;
	private final String content;

	public ChatMessageEvent(String from, String to, String content) {
		this.from = from;
		this.to = to;
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getContent() {
		return content;
	}
}
