package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * The Class PickupItemEvent.
 * @author Arrowzftw
 */
public final class PickupItemEvent extends Event {

	/**
	 * The item id.
	 */
	private final int itemId;

	/**
	 * The items coord.
	 */
	private final int x;

	/**
	 * The items coord.
	 */
	private final int y;

	/**
	 * Create a new item.
	 * @param itemId the item id
	 * @param x the x
	 * @param y the y
	 */
	public PickupItemEvent(int itemId, int x, int y) {
		this.itemId = itemId;
		this.x = x;
		this.y = y;
	}

	/**
	 * Return the item id.
	 * @return {@link Integer}
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Return the item coord.
	 * @return {@link Integer}
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return the item coord.
	 * @return {@link Integer}
	 */
	public int getY() {
		return y;
	}
}