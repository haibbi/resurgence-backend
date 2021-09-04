package tr.com.milia.resurgence.quest;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface QuestRepository extends JpaRepository<QuestEntity, Long> {

	List<QuestEntity> findAllByPlayer_Name(String player);

	Stream<QuestEntity> streamAllByPlayerAndStatus(Player player, QuestStatus status);

	Optional<QuestEntity> findByIdAndPlayer_Name(Long id, String player);

	Optional<QuestEntity> findByPlayerAndQuest(Player player, Quests quest);

}
