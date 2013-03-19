package org.apollo.game.event.impl;

/**
 * An {@link MagicEvent} that is sent when the player attempts to cast magic
 * onto another.
 * @author Steve
 */
public final class MagicOnPlayerEvent extends MagicEvent {

	/**
	 * Creates the magic on player event.
	 * @param playerId The player index that was clicked.
	 * @param spellId The spell id that was used.
	 */
	public MagicOnPlayerEvent(int playerId, int spellId) {
		super(playerId, spellId, 0);
	}
}
