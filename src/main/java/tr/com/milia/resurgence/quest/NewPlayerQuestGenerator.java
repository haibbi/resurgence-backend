package tr.com.milia.resurgence.quest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tr.com.milia.resurgence.player.PlayerCreatedEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class NewPlayerQuestGenerator {

	private static final Logger log = LoggerFactory.getLogger(NewPlayerQuestGenerator.class);
	private final QuestRepository repository;

	public NewPlayerQuestGenerator(QuestRepository repository) {
		this.repository = repository;
	}

	@EventListener(PlayerCreatedEvent.class)
	public void on(PlayerCreatedEvent event) {
		var player = event.getSource();
		var playerQuests = repository.findAllByPlayer_Name(player.getName());
		if (!playerQuests.isEmpty()) {
			log.warn("New player[{}] created with quests. Total quest count {}", player.getName(), playerQuests.size());
		}
		var currentQuests = playerQuests.stream().map(QuestEntity::getName).collect(Collectors.toSet());
		Arrays.stream(Quests.values())
			.filter(q -> !currentQuests.contains(q.name()))
			.forEach(q -> repository.save(new QuestEntity(player, q)));
	}

}
