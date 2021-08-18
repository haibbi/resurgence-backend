package tr.com.milia.resurgence.quest;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tr.com.milia.resurgence.player.Player;
import tr.com.milia.resurgence.player.PlayerService;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Lazy(value = false)
public class PlayerQuestInitializer {

	public PlayerQuestInitializer(QuestRepository questRepo, PlayerService playerService) {
		final var allPlayer = playerService.allPlayer();
		final var questNames = Arrays.stream(Quests.values())
			.collect(Collectors.toSet());

		// check all player
		for (Player player : allPlayer) {
			for (Quests q : questNames) {
				if (questRepo.findByPlayerAndQuest(player, q).isEmpty()) {
					// if player does not have this quest, add.
					questRepo.save(new QuestEntity(player, q));
				}
			}
		}
	}

}

