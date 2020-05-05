package tr.com.milia.resurgence.family;

import java.time.Instant;

class AnnouncementResponse {
	private final Long id;
	private final String family;
	private final String player;
	private final String title;
	private final String content;
	private final boolean general;
	private final Instant time;

	public AnnouncementResponse(Announcement announcement) {
		id = announcement.getId();
		family = announcement.getFamily().getName();
		player = announcement.getPlayer().getName();
		title = announcement.getTitle();
		content = announcement.getContent();
		general = announcement.isGeneral();
		time = announcement.getTime();
	}

	public Long getId() {
		return id;
	}

	public String getFamily() {
		return family;
	}

	public String getPlayer() {
		return player;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public boolean isGeneral() {
		return general;
	}

	public Instant getTime() {
		return time;
	}
}
