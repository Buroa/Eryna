package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that sends users to the player's ignore list.
 * @author Steve
 */
public final class SendIgnoreEvent extends Event {

	/**
	 * The friends we are ignoring.
	 */
	private final long[] friends;

	/**
	 * Creates the new send ignore event.
	 * @param friends The friends we are sending to the player's ignore list.
	 */
	public SendIgnoreEvent(long[] friends) {
		this.friends = friends;
	}

	/**
	 * Gets the friends to ignore.
	 * @return The friends to ignore.
	 */
	public long[] getFriends() {
		return friends;
	}

}
