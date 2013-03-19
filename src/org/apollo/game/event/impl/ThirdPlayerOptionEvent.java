package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that handles the third player option event.
 * @author Steve
 */
public final class ThirdPlayerOptionEvent extends PlayerOptionEvent {

	/**
	 * Create a new third player option event.
	 * @param otherId The other player id.
	 */
	public ThirdPlayerOptionEvent(int otherId) {
		super(3, otherId);
	}
}
