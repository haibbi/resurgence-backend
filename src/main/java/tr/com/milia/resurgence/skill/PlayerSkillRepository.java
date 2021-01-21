package tr.com.milia.resurgence.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerSkillRepository extends JpaRepository<PlayerSkill, Player> {

	Optional<PlayerSkill> findById_PlayerAndId_Skill(Player player, Skill skill);

	List<PlayerSkill> findAllById_Player_Name(String player);

}
