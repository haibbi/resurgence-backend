package tr.com.milia.resurgence.chat;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.time.Instant;

@Entity
public class ReportedMail {

	@Id
	private Long id;

	@MapsId
	@ManyToOne
	private Mail mail;

	@ManyToOne
	private Player from;

	@ManyToOne
	private Player to;

	private String content;

	private Instant time;

	public ReportedMail() {
	}

	public ReportedMail(Mail mail) {
		this.mail = mail;
		from = mail.getFrom();
		to = mail.getTo();
		content = mail.getContent();
		time = mail.getTime();
	}

	public Long getId() {
		return id;
	}

	public Mail getMail() {
		return mail;
	}
}
