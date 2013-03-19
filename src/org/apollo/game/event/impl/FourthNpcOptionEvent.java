package org.apollo.game.event.impl;

/**
 * An {@link NpcOptionEvent} for when a player clicks on a npc.
 * @author Steve
 */
public final class FourthNpcOptionEvent extends NpcOptionEvent {

	/**
	 * Creates a new fourth npc option event.
	 * @param npcIndex The npc that was clicked.
	 */
	public FourthNpcOptionEvent(int npcIndex) {
		super(4, npcIndex);
	}

}
