package org.apollo.game.event.impl;

/**
 * An {@link NpcOptionEvent} for the second click npc.
 * @author Steve
 */
public final class SecondNpcOptionEvent extends NpcOptionEvent {

	/**
	 * Create a new second npc option event.
	 * @param slot The slot that was found.
	 */
	public SecondNpcOptionEvent(int slot) {
		super(2, slot);
	}
}
