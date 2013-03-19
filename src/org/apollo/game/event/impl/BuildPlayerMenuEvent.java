package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} for building a player menu upon right click.
 * @author Steve
 */
public final class BuildPlayerMenuEvent extends Event {

	/**
	 * The place in the menu.
	 */
	private final int id;

	/**
	 * Flag to check if the menu text should be first.
	 */
	private final boolean isFirst;

	/**
	 * The text to display on the menu.
	 */
	private final String message;

	/**
	 * Build a player menu.
	 * @param id The place in the menu.
	 * @param isFirst Flag to check if the menu text should be first.
	 * @param message The text to display on the menu.
	 */
	public BuildPlayerMenuEvent(int id, boolean isFirst, String message) {
		this.id = id;
		this.isFirst = isFirst;
		this.message = message;
	}

	/**
	 * Gets the place in the menu.
	 * @return The place in the menu.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the text to display on the menu.
	 * @return The text to display on the menu.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Flag to check if the menu text should be first.
	 * @return True if first in menu, false if not.
	 */
	public boolean isFirst() {
		return isFirst;
	}
}
