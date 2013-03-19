package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that notifies a client about the messaging server.
 * @author Steve
 */
public final class PrivateChatLoadedEvent extends Event {

	/**
	 * Private chat id.
	 */
	private final int id;

	/**
	 * Load a private chat event.
	 * @param id The loading id.
	 */
	public PrivateChatLoadedEvent(int id) {
		this.id = id;
	}

	/**
	 * Return the private chat id.
	 * @return {@link Integer}
	 */
	public int getId() {
		return id;
	}
}
