package tr.com.milia.resurgence.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.Optional;

public interface PlayerSkillRepository extends JpaRepository<PlayerSkill, Player> {

	Optional<PlayerSkill> findByPlayerAndSkill(Player player, Skill skill);

}
