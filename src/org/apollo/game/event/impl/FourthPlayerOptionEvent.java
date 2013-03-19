package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that handles the fourth player option event.
 * @author Steve
 */
public class FourthPlayerOptionEvent extends PlayerOptionEvent {

	/**
	 * Create a new fourth player option event.
	 * @param otherId The other player id.
	 */
	public FourthPlayerOptionEvent(int otherId) {
		super(4, otherId);
	}
}
