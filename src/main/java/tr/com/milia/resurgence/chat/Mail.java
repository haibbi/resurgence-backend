package tr.com.milia.resurgence.chat;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class Mail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Player from;

	@ManyToOne
	private Player to;

	private String content;

	private Instant time;

	private boolean read;

	private boolean deleted;

	private boolean deletedBySender;

	public Mail() {
	}

	public Mail(Player from, Player to, String content) {
		if (from.getName().equals(to.getName())) throw new SelfMailException();
		this.from = from;
		this.to = to;
		this.content = content;
		time = Instant.now();
	}

	public void markAsRead() {
		read = true;
	}

	public void markAsDeleted() {
		deleted = true;
	}

	public void markAsDeletedBySender() {
		deletedBySender = true;
	}

	public Long getId() {
		return id;
	}

	public Player getFrom() {
		return from;
	}

	public Player getTo() {
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

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isDeletedBySender() {
		return deletedBySender;
	}
}
