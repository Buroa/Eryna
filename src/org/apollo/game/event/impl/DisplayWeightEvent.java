package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that sends displays the current weight of the player.
 * @author Steve
 */
public final class DisplayWeightEvent extends Event {

	/**
	 * The weight of the player.
	 */
	private final int weight;

	/**
	 * Creates the display weight event.
	 * @param weight The player's current weight.
	 */
	public DisplayWeightEvent(int weight) {
		this.weight = weight;
	}

	/**
	 * Gets the weight of the player.
	 * @return The weight of the player.
	 */
	public int getWeight() {
		return weight;
	}
}
