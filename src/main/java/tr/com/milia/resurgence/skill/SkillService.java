package tr.com.milia.resurgence.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.player.PlayerCreatedEvent;
import tr.com.milia.resurgence.task.TaskSucceedResult;

@Service
public class SkillService {

	private static final Logger log = LoggerFactory.getLogger(SkillService.class);

	private final PlayerSkillRepository repository;

	public SkillService(PlayerSkillRepository repository) {
		this.repository = repository;
	}

	@EventListener(value = PlayerCreatedEvent.class)
	public void onPlayerCreatedEvent(PlayerCreatedEvent event) {
		var player = event.getSource();
		repository.save(new PlayerSkill(player, Skill.SNEAK, 50));
	}

	@EventListener(TaskSucceedResult.class)
	@Order(Ordered.HIGHEST_PRECEDENCE + 2)
	public void onTaskSucceedResult(TaskSucceedResult result) {
		log.debug("Task Succeed Result {}", result);
		var player = result.getPlayer();
		result.getSkillGain().forEach(skill -> repository.findByPlayerAndSkill(player, skill).ifPresentOrElse(
			playerSkill -> playerSkill.learn(0.1),
			() -> repository.save(new PlayerSkill(player, skill, 0.1))
		));
	}

}
