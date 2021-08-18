package tr.com.milia.resurgence.quest;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.milia.resurgence.item.PlayerItemService;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.task.Task;
import tr.com.milia.resurgence.task.TaskLog;
import tr.com.milia.resurgence.task.TaskLogService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Lazy(value = false)
public class QuestService {

	private final QuestRepository repository;
	private final PlayerItemService playerItemService;
	private final TaskLogService taskLogService;

	public QuestService(QuestRepository repository,
						PlayerItemService playerItemService,
						TaskLogService taskLogService) {
		this.repository = repository;
		this.playerItemService = playerItemService;
		this.taskLogService = taskLogService;
	}

	List<QuestEntity> playerQuests(String player) {
		return repository.findAllByPlayer_Name(player);
	}

	boolean isUnlocked(QuestEntity questEntity) {
		final var player = questEntity.getPlayer();
		return questEntity.getQuest().isUnlocked(player, completedQuests(player));
	}

	boolean canComplete(QuestEntity questEntity) {
		final var quest = questEntity.getQuest();
		final var player = questEntity.getPlayer();
		final var completedQuests = completedQuests(player);
		final var completedTasks = completedTasks(player, questEntity);
		return quest.canComplete(player, completedQuests, completedTasks);
	}

	@Transactional
	QuestEntity complete(Long id, String playerName) {
		var questEntity = repository.findByIdAndPlayer_Name(id, playerName).orElseThrow();
		if (!questEntity.isCompleted()) {
			throw new QuestAlreadyCompletedException();
		}
		if (!isUnlocked(questEntity)) {
			throw new LockedQuestException();
		}

		var quest = questEntity.getQuest();
		var player = questEntity.getPlayer();

		if (!this.canComplete(questEntity)) {
			throw new QuestCanNotCompleteYetException();
		}

		final var consumeItemCompleteRequirements = quest.consumeItemCompleteRequirements();
		final var experienceReward = quest.experienceReward();
		final var itemRewards = quest.itemRewards();

		consumeItemCompleteRequirements.forEach(icr ->
			playerItemService.removeItem(player, icr.item(), icr.quantity()));
		player.gainEXP(experienceReward.experience());
		itemRewards.forEach(ir -> playerItemService.addItem(player, ir.item(), ir.quantity()));
		questEntity.complete();
		return questEntity;
	}

	private Set<Quests> completedQuests(Player player) {
		return repository.streamAllByPlayerAndStatus(player, QuestStatus.DONE)
			.map(QuestEntity::getQuestIdentifier)
			.collect(Collectors.toSet());
	}

	private Map<Task, Long> completedTasks(Player player, QuestEntity questEntity) {
		return taskLogService.allPerformedTaskSince(player, questEntity.createdTime()).stream()
			.collect(Collectors.groupingBy(TaskLog::getTask, Collectors.counting()));
	}

}
