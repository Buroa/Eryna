package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} which sends the run energy to the client.
 * @author Steve
 */
public class UpdateRunEnergyEvent extends Event {

	/**
	 * The run energy.
	 */
	private final int energy;

	/**
	 * Creates a new send run event.
	 * @param energy The energy to send.
	 */
	public UpdateRunEnergyEvent(int energy) {
		this.energy = energy;
	}

	/**
	 * Gets the run energy.
	 * @return The run energy.
	 */
	public int getEnergy() {
		return energy;
	}
}