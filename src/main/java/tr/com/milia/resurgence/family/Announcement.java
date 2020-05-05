package tr.com.milia.resurgence.family;

import tr.com.milia.resurgence.player.Player;

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

	@ManyToOne
	private Player player;

	private String title;

	private String content;

	// is public
	private boolean general;

	private Instant time;

	public Announcement() {
	}

	public Announcement(Family family, Player player, String title, String content, boolean general) {
		this.family = family;
		this.player = player;
		this.title = title;
		this.content = content;
		this.general = general;
		time = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public Family getFamily() {
		return family;
	}

	public Player getPlayer() {
		return player;
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

	public boolean isGeneral() {
		return general;
	}

	public void setGeneral(boolean general) {
		this.general = general;
	}

	public Instant getTime() {
		return time;
	}
}
