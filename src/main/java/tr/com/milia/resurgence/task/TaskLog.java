package tr.com.milia.resurgence.task;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Entity
public class TaskLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private Task task;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Player createdBy;

	@Column(nullable = false)
	private Instant createdDate;

	public TaskLog() {
	}

	public TaskLog(Task task, Player player) {
		this.task = task;
		createdBy = player;
		createdDate = Instant.now();
	}

	@Transient
	boolean isExpired() {
		return Instant.now().isAfter(createdDate.plus(task.getDuration()));
	}

	@Transient
	Duration durationToLeft() {
		return Duration.between(Instant.now(), createdDate.plus(task.getDuration()));
	}

}
