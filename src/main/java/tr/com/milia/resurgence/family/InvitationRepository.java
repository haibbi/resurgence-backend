package tr.com.milia.resurgence.family;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	List<Invitation> findAllByPlayer(Player player);

	List<Invitation> findAllByFamily(Family family);

	List<Invitation> findAllByPlayerOrFamily(Player player, Family family);

	Optional<Invitation> findByPlayerAndFamily(Player player, Family family);

	void deleteAllByPlayer(Player player);

	void deleteByIdAndPlayer_Name(Long id, String playerName);
}
