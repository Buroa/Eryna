package org.apollo.game.event.impl;

/**
 * An {@link NpcOptionEvent} for the first click npc.
 * @author Steve
 */
public final class FirstNpcOptionEvent extends NpcOptionEvent {

	/**
	 * Create a new first npc option event.
	 * @param slot The slot that was found.
	 */
	public FirstNpcOptionEvent(int slot) {
		super(1, slot);
	}
}
