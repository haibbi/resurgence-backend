package tr.com.milia.resurgence.skill;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.player.PlayerCreatedEvent;

@Service
public class SkillService {

	private final PlayerSkillRepository repository;

	public SkillService(PlayerSkillRepository repository) {
		this.repository = repository;
	}

	@EventListener(value = PlayerCreatedEvent.class)
	public void onPlayerCreatedEvent(PlayerCreatedEvent event) {
		var player = event.getSource();
		repository.save(new PlayerSkill(player, Skill.SNEAK, 0));
	}

}
