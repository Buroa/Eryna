package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * Item used on object event.
 * @author Steve
 */
public class ItemUsedOnObjectEvent extends Event {

	/**
	 * The object being clicked.
	 */
	private final int object;

	/**
	 * The item slot.
	 */
	private final int slot;

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * The object x.
	 */
	private final int x;

	/**
	 * The object y.
	 */
	private final int y;

	/**
	 * Create a new item used on object event.
	 * @param object the object
	 * @param slot the slot
	 * @param id the id
	 * @param x the x
	 * @param y the y
	 */
	public ItemUsedOnObjectEvent(int object, int slot, int id, int x, int y) {
		this.object = object;
		this.slot = slot;
		this.id = id;
		this.x = x;
		this.y = y;
	}

	/**
	 * The item id.
	 * @return {@link Integer}
	 */
	public int getId() {
		return id;
	}

	/**
	 * The object.
	 * @return {@link Integer}
	 */
	public int getObject() {
		return object;
	}

	/**
	 * The object position.
	 * @return {@link Position}
	 */
	public Position getPosition() {
		return new Position(x, y, 0);
	}

	/**
	 * The item slot.
	 * @return {@link Integer}
	 */
	public int getSlot() {
		return slot;
	}
}
