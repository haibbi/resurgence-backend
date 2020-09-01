package tr.com.milia.resurgence.family;

import java.time.Instant;

class AnnouncementResponse {
	private final Long id;
	private final String title;
	private final String content;
	private final boolean secret;
	private final Instant time;

	public AnnouncementResponse(Announcement announcement) {
		id = announcement.getId();
		title = announcement.getTitle();
		content = announcement.getContent();
		secret = announcement.isSecret();
		time = announcement.getTime();
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

	public boolean isSecret() {
		return secret;
	}

	public Instant getTime() {
		return time;
	}
}
