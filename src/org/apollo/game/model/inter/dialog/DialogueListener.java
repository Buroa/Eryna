package org.apollo.game.model.inter.dialog;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.InterfaceListener;

/**
 * An {@link InterfaceListener} which also listens for dialogue-specific events
 * (i.e. clicking buttons).
 * @author Chris Fletcher
 */
public interface DialogueListener extends InterfaceListener {

	/**
	 * Called when the player has clicked the "Click here to continue" button on
	 * a chatting dialogue.
	 * @param player The player that clicked the button.
	 */
	public void continued(Player player);
}