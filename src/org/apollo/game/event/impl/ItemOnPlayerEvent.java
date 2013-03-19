package org.apollo.game.event.impl;

/**
 * An {@link InventoryItemEvent} that is sent when a player uses an item on
 * another player.
 * @author Steve
 */
public final class ItemOnPlayerEvent extends InventoryItemEvent {

	/**
	 * The player that was used.
	 */
	private final int playerIndex;

	/**
	 * Creates the item on player event.
	 * @param interfaceId The interface id.
	 * @param playerIndex The player index.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public ItemOnPlayerEvent(int interfaceId, int playerIndex, int id, int slot) {
		super(1, interfaceId, id, slot);
		this.playerIndex = playerIndex;
	}

	/**
	 * Gets the player index.
	 * @return The player index.
	 */
	public int getPlayerIndex() {
		return playerIndex;
	}
}
