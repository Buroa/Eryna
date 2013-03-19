package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Position;

/**
 * An {@link Event} that sends items to the map tile.
 * @author Steve
 */
public abstract class GroundEvent extends MapEvent {

	/**
	 * The ground item.
	 */
	private final GroundItem item;

	/**
	 * Create a new ground item event.
	 * @param position The position of this ground item.
	 * @param item The ground item.
	 */
	public GroundEvent(Position position, GroundItem item) {
		super(position);
		this.item = item;
	}

	/**
	 * Gets the event id.
	 * @return The event id.
	 */
	@Override
	public int getEventId() {
		return 21;
	}

	/**
	 * Gets the ground item.
	 * @return The ground item.
	 */
	public GroundItem getGroundItem() {
		return item;
	}
}