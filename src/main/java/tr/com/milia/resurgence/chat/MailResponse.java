package tr.com.milia.resurgence.chat;

import java.time.Instant;

class MailResponse {

	private final Long id;
	private final String from;
	private final String to;
	private final String content;
	private final Instant time;
	private final boolean read;

	public MailResponse(Mail mail) {
		id = mail.getId();
		from = mail.getFrom().getName();
		to = mail.getTo().getName();
		content = mail.getContent();
		time = mail.getTime();
		read = mail.isRead();
	}

	public Long getId() {
		return id;
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

	public Instant getTime() {
		return time;
	}

	public boolean isRead() {
		return read;
	}
}
