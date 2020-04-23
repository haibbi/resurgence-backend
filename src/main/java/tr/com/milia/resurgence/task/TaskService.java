package tr.com.milia.resurgence.task;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.item.Item;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

	private final double PH = .10;
	private final PlayerService playerService;
	private final ApplicationEventPublisher eventPublisher;

	public TaskService(PlayerService playerService, ApplicationEventPublisher eventPublisher) {
		this.playerService = playerService;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public TaskResult perform(Task task, String username, @Nullable Map<Item, Integer> selectedItems) {
		var player = playerService.findByUsername(username).orElseThrow(PlayerNotFound::new);

		selectedItems = selectedItems == null ? Collections.emptyMap() : selectedItems;

		eventPublisher.publishEvent(new TaskStartedEvent(player, task, selectedItems));
		TaskResult taskResult = performInternal(task, player, selectedItems);
		eventPublisher.publishEvent(taskResult);

		return taskResult;
	}

	private TaskResult performInternal(Task task, Player player, Map<Item, Integer> selectedItems) {
		var playerSkills = player.getSkills();

		// todo level'den geleni ekle
		// skill contribution
		double sum = playerSkills.stream().mapToDouble(playerSkill -> {
			BigDecimal expertise = playerSkill.getExpertise();
			double contribution = playerSkill.getSkill().contribution();
			return expertise.multiply(BigDecimal.valueOf(contribution))
				.divide(BigDecimal.valueOf(100), RoundingMode.CEILING)
				.doubleValue();
		}).sum();

		// item contribution
		sum += selectedItems.entrySet().stream()
			.mapToInt(e -> e.getKey().getSkillsContribution(task.getAuxiliary()) * e.getValue())
			.sum();

		double success = sum / task.getDifficulty();

		double random = RandomUtils.random();

		if (random > success) {
			return new TaskFailedResult(player, task, selectedItems);
		}

		double gainRatio = random / success;

		int experienceGain = (int) (task.getExperienceGain() * gainRatio);
		var moneyGain = task.getMoneyGain().multiply(BigDecimal.valueOf(gainRatio));
		var gainedSkills = task.getSkillGain().stream()
			.filter(skill -> RandomUtils.random() <= PH)
			.collect(Collectors.toSet());
		var drop = task.getDrop().entrySet().stream()
			.filter(item -> RandomUtils.random() <= PH)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		return new TaskSucceedResult(player, task, experienceGain, moneyGain, gainedSkills, drop, selectedItems);
	}

}
