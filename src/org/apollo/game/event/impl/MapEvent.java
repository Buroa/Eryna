package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An {@link Event} for map events.
 * @author Steve
 */
public abstract class MapEvent extends Event {

	/**
	 * The position of the map event.
	 */
	private final Position position;

	/**
	 * The offsetY.
	 */
	private int offsetY = 0;

	/**
	 * The offsetX.
	 */
	private int offsetX = 0;

	/**
	 * Creates a new map event.
	 * @param position The position of the map event.
	 */
	public MapEvent(Position position) {
		this.position = position;
	}

	/**
	 * Creates a new map event.
	 * @param position The position of the map event.
	 * @param offsetX The offset x.
	 * @param offsetY The offset y.
	 */
	public MapEvent(Position position, int offsetX, int offsetY) {
		this(position);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	@Override
	public int getEventId() {
		return 1;
	}

	/**
	 * Gets the offset x.
	 * @return The offset x.
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * Gets the offset y.
	 * @return The offset y.
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * Gets the position of the map event.
	 * @return The position of the map event.
	 */
	public Position getPosition() {
		return position;
	}
}
