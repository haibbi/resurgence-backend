package tr.com.milia.resurgence.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerSkillRepository extends JpaRepository<PlayerSkill, Player> {

	Optional<PlayerSkill> findByPlayer_Name(String playerName);

}
