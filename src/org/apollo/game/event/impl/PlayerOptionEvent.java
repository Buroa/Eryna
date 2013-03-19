package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;

/**
 * An {@link Event} which represents some sort of player action.
 * @author Steve
 */
public abstract class PlayerOptionEvent extends Event {

	/**
	 * The option number (1-4).
	 */
	private final int option;

	/**
	 * The other player id.
	 */
	private final int otherId;

	/**
	 * Creates the item action event.
	 * @param option The option number.
	 * @param otherId The other player.
	 */
	public PlayerOptionEvent(int option, int otherId) {
		this.option = option;
		this.otherId = otherId;
	}

	/**
	 * Gets the option number.
	 * @return The option number.
	 */
	public int getOption() {
		return option;
	}

	/**
	 * Gets the player that was clicked.
	 * @return The player that was clicked.
	 */
	public Player getPlayer() {
		return (Player) World.getWorld().getPlayerRepository().get(otherId - 1);
	}

	/**
	 * Gets the other player id.
	 * @return The other player id.
	 */
	public int getPlayerId() {
		return otherId;
	}
}
