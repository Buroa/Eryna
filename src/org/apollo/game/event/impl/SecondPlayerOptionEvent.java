package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that handles the second player option event.
 * @author Steve
 */
public class SecondPlayerOptionEvent extends PlayerOptionEvent {

	/**
	 * Create a new second player option event.
	 * @param otherId The other player id.
	 */
	public SecondPlayerOptionEvent(int otherId) {
		super(2, otherId);
	}
}
