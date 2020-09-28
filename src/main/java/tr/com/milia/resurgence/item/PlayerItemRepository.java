package tr.com.milia.resurgence.item;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerItemRepository extends JpaRepository<PlayerItem, Long> {

	Optional<PlayerItem> findById_PlayerAndId_Item(Player player, Item item);

	Optional<PlayerItem> findById_PlayerAndId_ItemAndQuantityGreaterThanEqual(Player player, Item item, long quantity);

	List<PlayerItem> findAllById_Player(Player player);

}
