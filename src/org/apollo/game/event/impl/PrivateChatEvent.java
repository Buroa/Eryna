package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} for when a player sends a message to a friend.
 * @author Steve
 */
public final class PrivateChatEvent extends Event {

	/**
	 * The friend.
	 */
	private final long friend;

	/**
	 * The compressed message.
	 */
	private final byte[] message;

	/**
	 * Create a new private chat event.
	 * @param friend The friend to send this message too.
	 * @param message The decoded message.
	 */
	public PrivateChatEvent(long friend, byte[] message) {
		this.friend = friend;
		this.message = message;
	}

	/**
	 * Gets the friend.
	 * @return The friend.
	 */
	public long getFriend() {
		return friend;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public byte[] getMessage() {
		return message;
	}
}
