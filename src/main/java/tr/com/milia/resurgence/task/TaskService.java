package tr.com.milia.resurgence.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(TaskService.class);

	protected final ApplicationEventPublisher eventPublisher;
	private final double PH = .10;
	private final PlayerService playerService;

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
		TaskInfo.Builder info = TaskInfo.Builder.newBuilder()
			.task(task)
			.player(player.getName())
			.selectedItems(SelectedItem.fromMap(selectedItems));

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
		info.skillContribution(sum);

		// item contribution
		sum += selectedItems.entrySet().stream()
			.mapToLong(selectedItem -> {
				Item item = selectedItem.getKey();
				Long quantity = selectedItem.getValue();
				return item.getSkillsContribution(task.getAuxiliary()) * quantity;
			})
			.sum();
		info.itemContribution(sum);

		// passive item contribution
		sum += player.getItems().stream()
			.filter(playerItem -> Item.PASSIVE.contains(playerItem.getItem()))
			.mapToLong(p -> p.getItem().getSkillsContribution(task.getAuxiliary()))
			.sum();
		info.passiveItemContribution(sum);

		double success = sum / task.getDifficulty();

		double random = RandomUtils.random();

		info.successRatio(success).randomRatio(random);
		if (random > success) {
			log.debug(info.succeed(false).build().toString());
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

		log.debug(info.gainRatio(gainRatio).succeed(true).build().toString());
		return new TaskSucceedResult(player, task, experienceGain, moneyGain, gainedSkills, drop, selectedItems);
	}

	protected Player findPlayer(String name) {
		return playerService.findByName(name).orElseThrow(PlayerNotFound::new);
	}

}
