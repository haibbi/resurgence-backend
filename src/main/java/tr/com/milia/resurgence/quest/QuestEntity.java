package tr.com.milia.resurgence.quest;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name = "quests")
public class QuestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;

	@Enumerated(EnumType.STRING)
	private Quests quest;

	@Enumerated(EnumType.STRING)
	private QuestStatus status;

	private Instant startTime;

	public QuestEntity() {
	}

	public QuestEntity(Player player, Quests quest) {
		this.player = player;
		this.quest = quest;
		this.status = QuestStatus.PENDING;
	}

	public Long getId() {
		return id;
	}

	Player getPlayer() {
		return player;
	}

	Quests getQuestIdentifier() {
		return this.quest;
	}

	Quest getQuest() {
		return quest.getQuest();
	}

	String getName() {
		return quest.name();
	}

	int ordinal() {
		return quest.ordinal();
	}

	Quests getQuestEnum() {
		return this.quest;
	}

	QuestStatus getStatus() {
		return status;
	}

	public boolean isCompleted() {
		return QuestStatus.COMPLETED_STATUS.contains(this.status);
	}

	public void complete() {
		this.status = QuestStatus.DONE;
	}

	public void cancel() {
		this.status = QuestStatus.CANCELED;
	}

	void markAsStarted() {
		if (this.startTime == null) {
			this.status = QuestStatus.IN_PROGRESS;
			this.startTime = Instant.now();
		}
	}

	public Optional<Instant> getStartTime() {
		return Optional.ofNullable(startTime);
	}
}
