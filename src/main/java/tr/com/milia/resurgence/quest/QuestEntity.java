package tr.com.milia.resurgence.quest;

import tr.com.milia.resurgence.player.Player;

import javax.persistence.*;
import java.time.Instant;

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

	private Instant createdTime;

	public QuestEntity() {
	}

	public QuestEntity(Player player, Quests quest) {
		this.player = player;
		this.quest = quest;
		this.status = QuestStatus.PENDING;
		this.createdTime = Instant.now();
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

	QuestStatus getStatus() {
		return status;
	}

	public boolean isCompleted() {
		return !QuestStatus.COMPLETED_STATUS.contains(this.status);
	}

	public void complete() {
		this.status = QuestStatus.DONE;
	}

	public void cancel() {
		this.status = QuestStatus.CANCELED;
	}

	public Instant createdTime() {
		return this.createdTime;
	}
}
