package tr.com.milia.resurgence.task;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerNotFound;
import tr.com.milia.resurgence.player.PlayerService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Primary
public class TaskService {

	private final double PH = .10;
	private final PlayerService playerService;
	protected final ApplicationEventPublisher eventPublisher;

	public TaskService(PlayerService playerService, ApplicationEventPublisher eventPublisher) {
		this.playerService = playerService;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public TaskResult perform(Task task, String playerName, @Nullable Map<Item, Long> selectedItems) {
		var player = findPlayer(playerName);

		selectedItems = selectedItems == null ? Collections.emptyMap() : selectedItems;

		eventPublisher.publishEvent(new TaskStartedEvent(player, task, selectedItems));
		TaskResult taskResult = performInternal(task, player, selectedItems);
		eventPublisher.publishEvent(taskResult);

		return taskResult;
	}

	protected TaskResult performInternal(Task task, Player player, Map<Item, Long> selectedItems) {
		var playerSkills = player.getSkills();

		// todo level'den geleni ekle
		// skill contribution
		double sum = playerSkills.stream()
			.filter(playerSkill -> task.getAuxiliary().contains(playerSkill.getSkill()))
			.mapToDouble(playerSkill -> {
				BigDecimal expertise = playerSkill.getExpertise();
				double contribution = playerSkill.getSkill().contribution();
				return expertise.multiply(BigDecimal.valueOf(contribution))
					.divide(BigDecimal.valueOf(100), RoundingMode.CEILING)
					.doubleValue();
			})
			.sum();

		// item contribution
		sum += selectedItems.entrySet().stream()
			.mapToLong(e -> e.getKey().getSkillsContribution(task.getAuxiliary()) * e.getValue())
			.sum();

		double success = sum / task.getDifficulty();

		double random = RandomUtils.random();

		if (random > success) {
			return new TaskFailedResult(player, task, selectedItems);
		}

		double gainRatio = random / success;

		int experienceGain = (int) (task.getExperienceGain() * gainRatio);
		long moneyGain = (long) (task.getMoneyGain() * gainRatio);
		var gainedSkills = task.getSkillGain().stream()
			.filter(skill -> RandomUtils.random() <= PH)
			.collect(Collectors.toSet());
		var drop = task.getDrop().entrySet().stream() // just filter by random
			.filter(d -> RandomUtils.random() <= d.getValue().getRatio())
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		return new TaskSucceedResult(player, task, experienceGain, moneyGain, gainedSkills, drop, selectedItems);
	}

	protected Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}

}
