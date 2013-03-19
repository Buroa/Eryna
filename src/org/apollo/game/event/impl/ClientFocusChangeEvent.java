package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that triggers when a client focus changes.
 * @author Steve
 */
public final class ClientFocusChangeEvent extends Event {

	/**
	 * True if focused, false if not.
	 */
	private final boolean focus;

	/**
	 * Creates a new client focus change event.
	 * @param focus True if focused, false if not.
	 */
	public ClientFocusChangeEvent(boolean focus) {
		this.focus = focus;
	}

	/**
	 * Gets the focus flag.
	 * @return True if client is focused, false if not.
	 */
	public boolean isFocused() {
		return focus;
	}
}
