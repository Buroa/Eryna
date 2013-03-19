package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} which will display yells on a client.
 * @author Steve
 */
public final class YellEvent extends Event {

	/**
	 * The prefix.
	 */
	private final String prefix;

	/**
	 * The players name.
	 */
	private final String name;

	/**
	 * The players message.
	 */
	private final String message;

	/**
	 * The players rights.
	 */
	private final int rights;

	/**
	 * Creates a new yell event.
	 * @param prefix The prefix.
	 * @param name The players name.
	 * @param message The players message.
	 * @param rights THe players rights.
	 */
	public YellEvent(String prefix, String name, String message, int rights) {
		this.prefix = prefix;
		this.name = name;
		this.message = message;
		this.rights = rights;
	}

	/**
	 * Gets the players message.
	 * @return The players message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the players name.
	 * @return The players name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the prefix.
	 * @return The prefix.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Gets the players rights.
	 * @return The players rights.
	 */
	public int getRights() {
		return rights;
	}

}
