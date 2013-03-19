package org.apollo.game.event.impl;

/**
 * An {@link NpcOptionEvent} for the third npc click.
 * @author Steve
 */
public final class ThirdNpcOptionEvent extends NpcOptionEvent {

	/**
	 * Create a new third npc option event.
	 * @param slot The slot that was found.
	 */
	public ThirdNpcOptionEvent(int slot) {
		super(3, slot);
	}
}
