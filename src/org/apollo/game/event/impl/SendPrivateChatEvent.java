package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.World;

/**
 * An {@link Event} that sends a private message to another player on the
 * {@link World}
 * @author Steve
 */
public final class SendPrivateChatEvent extends Event {

	/**
	 * The friend.
	 */
	private final long from;

	/**
	 * The friend's rights.
	 */
	private final int rights;

	/**
	 * The compressed message.
	 */
	private final byte[] message;

	/**
	 * The last message id.
	 */
	private final int id;

	/**
	 * Creates a new send private chat event.
	 * @param from The sender.
	 * @param rights The rights of the sender.
	 * @param message The message.
	 * @param id The last message id.
	 */
	public SendPrivateChatEvent(long from, int rights, byte[] message, int id) {
		this.from = from;
		this.rights = rights;
		this.message = message;
		this.id = id;
	}

	/**
	 * Gets the friend.
	 * @return The friend.
	 */
	public long getFrom() {
		return from;
	}

	/**
	 * Gets the last chat id.
	 * @return The last chat id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public byte[] getMessage() {
		return message;
	}

	/**
	 * Gets the rights.
	 * @return The rights.
	 */
	public int getRights() {
		return rights;
	}

}
