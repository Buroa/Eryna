package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An {@link Event} which triggers when a player casts a spell on a object.
 * @author Steve
 */
public final class MagicOnGroundEvent extends Event {

	/**
	 * The position of the object.
	 */
	private final Position position;

	/**
	 * The spell being casted.
	 */
	private final int magicId;

	/**
	 * The item being casted upon.
	 */
	private final int itemId;

	/**
	 * Creates the new magic on object event.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param magicId The spell being casted.
	 * @param itemId The item being casted upon.
	 */
	public MagicOnGroundEvent(int x, int y, int magicId, int itemId) {
		this.position = new Position(x, y);
		this.magicId = magicId;
		this.itemId = itemId;
	}

	/**
	 * Gets the item being casted upon.
	 * @return The item being casted upon.
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the spell being casted.
	 * @return The spell being casted.
	 */
	public int getMagicId() {
		return magicId;
	}

	/**
	 * Gets the position of the object.
	 * @return The position of the object.
	 */
	public Position getPosition() {
		return position;
	}

}
