package tr.com.milia.resurgence.task;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.item.PlayerItem;
import tr.com.milia.resurgence.player.PlayerService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class TaskService {

	private final double PH = .10;
	private final PlayerService playerService;
	private final TaskLogService taskLogService;
	private final ApplicationEventPublisher eventPublisher;

	public TaskService(PlayerService playerService,
					   TaskLogService taskLogService,
					   ApplicationEventPublisher eventPublisher) {
		this.playerService = playerService;
		this.taskLogService = taskLogService;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public TaskResult perform(Task task, String username) {
		TaskResult taskResult = performInternal(task, username);
		eventPublisher.publishEvent(taskResult);
		return taskResult;
	}

	private TaskResult performInternal(Task task, String username) {
		var player = playerService.findByUsername(username).orElseThrow(PlayerNotFound::new);
		taskLogService.checkPerform(player, task);

		var playerSkills = player.getSkills();

		// todo level'den geleni ekle
		// skill contribution
		double sum = playerSkills.stream().mapToDouble(playerSkill -> {
			double expertise = playerSkill.getExpertise();
			double contribution = playerSkill.getSkill().contribution();
			return (contribution * expertise) / 100;
		}).sum();

		// item contribution
		sum += player.getItems().stream()
			.map(PlayerItem::getItem)
			.mapToInt(i -> i.getSkillsContribution(task.getAuxiliary()))
			.sum();

		double success = sum / task.getDifficulty();

		double random = RandomUtils.random();

		if (random > success) {
			return new TaskFailedResult(player, task);
		}

		double gainRatio = random / success;

		int experienceGain = (int) (task.getExperienceGain() * gainRatio);
		var moneyGain = task.getMoneyGain().multiply(BigDecimal.valueOf(gainRatio));
		var gainedSkills = task.getSkillGain().stream()
			.filter(skill -> RandomUtils.random() <= PH)
			.collect(Collectors.toSet());
		var drop = task.getDrop().stream()
			.filter(item -> RandomUtils.random() <= PH)
			.collect(Collectors.toSet());

		return new TaskSucceedResult(player, task, experienceGain, moneyGain, gainedSkills, drop);
	}

}
