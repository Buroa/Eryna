package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that opens a welcome screen.
 * @author Steve
 */
public final class OpenWelcomeScreenEvent extends Event {

	/**
	 * The days since the last recovery.
	 */
	private final int daysSinceLastRecovery;

	/**
	 * The number of unread messages.
	 */
	private final int unreadMessages;

	/**
	 * True if member, false if not.
	 */
	private final boolean isMember;

	/**
	 * The last logged ip.
	 */
	private final long lastLoggedIp;

	/**
	 * The last logged successful login.
	 */
	private final long lastLoggedIn;

	/**
	 * Opens the players welcome screen.
	 * @param daysSinceLastRecovery The days since the last recovery change.
	 * @param unreadMessages The number of unread messages.
	 * @param isMember True if member, false if not.
	 * @param lastLoggedIp The last logged in ip address.
	 * @param lastLoggedIn The last time the player has successfully logged in.
	 */
	public OpenWelcomeScreenEvent(int daysSinceLastRecovery, int unreadMessages, boolean isMember, long lastLoggedIp,
			long lastLoggedIn) {
		this.daysSinceLastRecovery = daysSinceLastRecovery;
		this.unreadMessages = unreadMessages;
		this.isMember = isMember;
		this.lastLoggedIp = lastLoggedIp;
		this.lastLoggedIn = lastLoggedIn;
	}

	/**
	 * Gets the days since last recovery.
	 * @return The days since last recovery.
	 */
	public int getDaysSinceLastRecovery() {
		return daysSinceLastRecovery;
	}

	/**
	 * Gets the last successful session.
	 * @return The last successful session.
	 */
	public long getLastLoggedIn() {
		return lastLoggedIn;
	}

	/**
	 * Gets the last logged ip address.
	 * @return The last logged ip address.
	 */
	public long getLastLoggedIp() {
		return lastLoggedIp;
	}

	/**
	 * Gets the number of unread messages.
	 * @return The number of unread messages.
	 */
	public int getUnreadMessages() {
		return unreadMessages;
	}

	/**
	 * Gets the members flag.
	 * @return True if member, false if not.
	 */
	public boolean isMember() {
		return isMember;
	}
}