package tr.com.milia.resurgence.notification;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notification_message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Player to;

	private String title;

	private String content;

	private Instant time;

	private boolean deleted;

	public Message() {
	}

	public Message(Player to, String title, String content) {
		this.to = to;
		this.title = title;
		this.content = content;
		time = Instant.now();
	}

	void delete() {
		deleted = true;
	}

	public Long getId() {
		return id;
	}

	public Player getTo() {
		return to;
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

	public boolean isDeleted() {
		return deleted;
	}
}
