package tr.com.milia.resurgence.skill;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.player.PlayerCreatedEvent;
import tr.com.milia.resurgence.task.TaskSucceedResult;

@Service
public class SkillService {

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
	public void onTaskSucceedResult(TaskSucceedResult result) {
		var player = result.getPlayer();
		result.getSkillGain().forEach(skill -> repository.findByPlayerAndSkill(player, skill).ifPresentOrElse(
			playerSkill -> playerSkill.learn(0.1),
			() -> repository.save(new PlayerSkill(player, skill, 0.1))
		));
	}

}
