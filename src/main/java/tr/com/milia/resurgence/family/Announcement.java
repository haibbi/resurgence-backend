package tr.com.milia.resurgence.family;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class Announcement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Family family;

	private String title;

	private String content;

	private boolean secret;

	private Instant time;

	public Announcement() {
	}

	public Announcement(Family family, String title, String content, boolean secret) {
		this.family = family;
		this.title = title;
		this.content = content;
		this.secret = secret;
		time = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public Family getFamily() {
		return family;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSecret() {
		return secret;
	}

	public void setSecret(boolean secret) {
		this.secret = secret;
	}

	public Instant getTime() {
		return time;
	}
}
