package org.apollo.game.model.inter;

import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Player;

/**
 * An {@code listener} for the {@link GroundItem}.
 * @author Steve
 */
public interface GroundItemListener {

	/**
	 * Called when the player has clicked on a ground item.
	 * @param player The player that has clicked on the ground item.
	 * @param item The item that has been clicked upon.
	 * @return {@code true} if the event handler should be broken.
	 */
	boolean itemClicked(Player player, GroundItem item);

}