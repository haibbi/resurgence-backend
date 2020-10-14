package tr.com.milia.resurgence.notification;

import java.time.Instant;

class MessageResponse {

	private final Long id;
	private final String title;
	private final String content;
	private final Instant time;

	public MessageResponse(Message message) {
		id = message.getId();
		title = message.getTitle();
		content = message.getContent();
		time = message.getTime();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Instant getTime() {
		return time;
	}
}
