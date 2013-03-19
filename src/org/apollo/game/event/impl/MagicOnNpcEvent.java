package org.apollo.game.event.impl;

/**
 * An {@link MagicEvent} for when a player casts a spell on the npc.
 * @author Steve
 */
public final class MagicOnNpcEvent extends MagicEvent {

	/**
	 * Creates the magic on npc event.
	 * @param playerId The player that was being casted upon.
	 * @param spellId The spell that was being casted.
	 */
	public MagicOnNpcEvent(int playerId, int spellId) {
		super(playerId, spellId, 1);
	}

}
