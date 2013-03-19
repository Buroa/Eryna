package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Npc;
import org.apollo.game.model.World;

/**
 * An {@link Event} which is sent by the client when the player has clicked an
 * option of an {@link Npc}.
 * @author Steve
 */
public abstract class NpcOptionEvent extends Event {

	/**
	 * The option that was clicked.
	 */
	private final int option;

	/**
	 * The slot that was found.
	 */
	private final int slot;

	/**
	 * Creates the npc option event.
	 * @param option The option that was clicked.
	 * @param slot The slot that was found.
	 */
	public NpcOptionEvent(int option, int slot) {
		this.option = option;
		this.slot = slot;
	}

	/**
	 * Gets the {@link Npc} that was clicked.
	 * @return The NPC that was clicked.
	 */
	public Npc getNpc() {
		return World.getWorld().getNpcRepository().forIndex(slot);
	}

	/**
	 * Gets the option that was clicked.
	 * @return The option that was clicked.
	 */
	public int getOption() {
		return option;
	}

	/**
	 * Gets the slot that was found.
	 * @return The slot that was found.
	 */
	public int getSlot() {
		return slot;
	}
}
