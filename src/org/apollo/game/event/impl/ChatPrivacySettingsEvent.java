package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that sends the chat privacy settings.
 * @author Steve
 */
public final class ChatPrivacySettingsEvent extends Event {

	/**
	 * The public chat setting.
	 */
	private final int publicChat;

	/**
	 * The private chat setting.
	 */
	private final int privateChat;

	/**
	 * The trade setting.
	 */
	private final int trade;

	/**
	 * Create a new chat privacy settings event.
	 * @param publicChat The public chat value.
	 * @param privateChat The private chat value.
	 * @param trade The trade value.
	 */
	public ChatPrivacySettingsEvent(int publicChat, int privateChat, int trade) {
		this.publicChat = publicChat;
		this.privateChat = privateChat;
		this.trade = trade;
	}

	/**
	 * Gets the private chat value.
	 * @return The private chat value.
	 */
	public int getPrivateChat() {
		return privateChat;
	}

	/**
	 * Gets the public chat value.
	 * @return The public chat value.
	 */
	public int getPublicChat() {
		return publicChat;
	}

	/**
	 * Gets the trade value.
	 * @return The trade value.
	 */
	public int getTrade() {
		return trade;
	}
}
