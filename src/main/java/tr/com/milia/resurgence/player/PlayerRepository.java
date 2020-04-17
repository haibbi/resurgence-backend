package tr.com.milia.resurgence.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface PlayerRepository extends JpaRepository<Player, Long> {

	Optional<Player> findByAccount_Email(String email);

	Optional<Player> findByName(String name);

}
