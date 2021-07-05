package tr.com.milia.resurgence.item;

import tr.com.milia.resurgence.player.Player;

@FunctionalInterface
public interface UsableItem {

	/**
	 * @param player who will use this item
	 * @return {@code true} if player used this item
	 */
	boolean useItem(Player player);

}
