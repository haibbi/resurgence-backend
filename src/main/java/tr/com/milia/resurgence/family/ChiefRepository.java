package tr.com.milia.resurgence.family;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

public interface ChiefRepository extends JpaRepository<Chief, Player> {
}
