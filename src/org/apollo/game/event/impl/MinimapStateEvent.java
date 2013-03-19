package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that changes the minimap state.
 * @author Steve
 */
public final class MinimapStateEvent extends Event {

	/**
	 * The minimap state.
	 */
	private final int state;

	/**
	 * Creates the minimap state event.
	 * @param state The state to set.
	 */
	public MinimapStateEvent(int state) {
		this.state = state;
	}

	/**
	 * Gets the state.
	 * @return The state.
	 */
	public int getState() {
		return state;
	}
}
