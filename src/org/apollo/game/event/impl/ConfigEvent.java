package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that sends config values to the client.
 * @author Steve
 */
public final class ConfigEvent extends Event {

	/**
	 * The config id.
	 */
	private final int id;

	/**
	 * The config value.
	 */
	private final int value;

	/**
	 * Create a new config event.
	 * @param id The config id.
	 * @param value The config state.
	 */
	public ConfigEvent(int id, int value) {
		this.id = id;
		this.value = value;
	}

	/**
	 * Gets the config id.
	 * @return The config id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the config value.
	 * @return The config value.
	 */
	public int getValue() {
		return value;
	}
}
