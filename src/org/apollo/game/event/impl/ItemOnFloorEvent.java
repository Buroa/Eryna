package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An {@link Event} that is sent when a player uses an item on another item
 * thats on the floor.
 * @author Steve
 */
public final class ItemOnFloorEvent extends InventoryItemEvent {

	/**
	 * The item on the floor.
	 */
	private final int floorId;

	/**
	 * The position of the item on the floor.
	 */
	private final Position position;

	/**
	 * Creates the item on floor event.
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The slot id.
	 * @param floorId The item on the floor id.
	 * @param position The position of the floor id.
	 */
	public ItemOnFloorEvent(int interfaceId, int id, int slot, int floorId, Position position) {
		super(1, interfaceId, id, slot);
		this.floorId = floorId;
		this.position = position;
	}

	/**
	 * Gets the item on the floor.
	 * @return The item on the floor.
	 */
	public int getFloorId() {
		return floorId;
	}

	/**
	 * Gets the position of the item on the floor.
	 * @return The position of the item on the floor.
	 */
	public Position getPosition() {
		return position;
	}
}