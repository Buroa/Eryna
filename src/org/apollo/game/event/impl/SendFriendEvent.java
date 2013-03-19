package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that notify's the player that their friend is online.
 * @author Steve
 */
public final class SendFriendEvent extends Event {

	/**
	 * The friend we are notifying about.
	 */
	private final long friend;

	/**
	 * The status of this friend (world).
	 */
	private final int status;

	/**
	 * Creates a new friend event.
	 * @param friend The friend we are notifying about.
	 * @param status The status of this friend.
	 */
	public SendFriendEvent(long friend, int status) {
		this.friend = friend;
		this.status = status;
	}

	/**
	 * Gets the friend we are notifying about.
	 * @return The friend we are notifying about.
	 */
	public long getFriend() {
		return friend;
	}

	/**
	 * Gets the status of this friend.
	 * @return The status of this friend.
	 */
	public int getStatus() {
		return status;
	}

}
