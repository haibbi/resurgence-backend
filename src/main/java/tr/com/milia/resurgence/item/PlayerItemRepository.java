package tr.com.milia.resurgence.item;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.Optional;

public interface PlayerItemRepository extends JpaRepository<PlayerItem, Long> {

	Optional<PlayerItem> findByPlayerAndItem(Player player, Item item);

}
