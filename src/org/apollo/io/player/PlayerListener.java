package org.apollo.io.player;

import org.apollo.game.model.Player;

/**
 * An {@code listener} that executes when a player is logged in or out.
 * @author Buroa
 */
public abstract class PlayerListener {

	/**
	 * Executed upon a player login.
	 * @param player The player logging in.
	 */
	public abstract void login(Player player);

	/**
	 * Executed upon a player logging out.
	 * @param player The player logging out.
	 */
	public abstract void logout(Player player);

}
