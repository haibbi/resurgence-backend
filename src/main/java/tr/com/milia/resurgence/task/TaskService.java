package tr.com.milia.resurgence.task;

import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.RandomUtils;
import tr.com.milia.resurgence.player.PlayerService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class TaskService {

	private final double PH = .10;
	private final PlayerService playerService;

	public TaskService(PlayerService playerService) {
		this.playerService = playerService;
	}

	@Transactional
	public TaskResult perform(Task task, String username) {
		var player = playerService.findByUsername(username).orElseThrow(PlayerNotFound::new);

		var playerSkills = player.getSkills();

		// todo item'lardan ve level'den geleni ekle
		double sum = playerSkills.stream().mapToDouble(playerSkill -> {
			double expertise = playerSkill.getExpertise();
			double contribution = playerSkill.getSkill().contribution();
			return (contribution * expertise) / 100;
		}).sum();

		double success = sum / task.getDifficulty();

		double random = RandomUtils.random();

		if (random > success) return new TaskFailedResult();

		double gainRatio = random / success;

		int experienceGain = (int) (task.getExperienceGain() * gainRatio);
		var moneyGain = task.getMoneyGain().multiply(BigDecimal.valueOf(gainRatio));
		var gainedSkills = task.getSkillGain().stream()
			.filter(skill -> RandomUtils.random() <= PH)
			.collect(Collectors.toSet());

		return new TaskSucceedResult(experienceGain, moneyGain, gainedSkills);
	}

}
