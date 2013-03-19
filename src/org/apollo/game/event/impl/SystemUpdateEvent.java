package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that sends a system update timer to the players.
 * @author Steve
 */
public final class SystemUpdateEvent extends Event {

	/**
	 * The time to be sent to the players.
	 */
	private final int time;

	/**
	 * Create a new system update event.
	 * @param time The time to be sent to the players.
	 */
	public SystemUpdateEvent(int time) {
		this.time = time;
	}

	/**
	 * Gets the time to send to the players.
	 * @return The time to send to the players.
	 */
	public int getTime() {
		return time;
	}
}
