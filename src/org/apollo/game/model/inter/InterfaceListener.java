package org.apollo.game.model.inter;

import org.apollo.game.model.Player;

/**
 * Listens to interface-related events.
 * @author Graham
 * @author Chris Fletcher
 */
public interface InterfaceListener {

	/**
	 * Called when the specified player clicked the specified button.
	 * @param player The player that clicked the button.
	 * @param button The interface id of the clicked button.
	 * @return {@code true} if the button event handler chain should be broken.
	 */
	boolean buttonClicked(Player player, int button);

	/**
	 * Called when the interface has been closed.
	 * @param player The player that closed the interface.
	 * @param manually Flag for if the interface was closed manually (by the
	 * player) or not (by the server),
	 * respectively {@code true} or {@code false}.
	 */
	public void interfaceClosed(Player player, boolean manually);
}
