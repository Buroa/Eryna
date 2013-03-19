package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An {@link Event} which represents a position change.
 * @author Steve
 */
public final class PositionEvent extends Event {

	/**
	 * The base.
	 */
	private final Position base;

	/**
	 * The position.
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
	 * Creates the event with the specified base and position.
	 * @param base The position base.
	 * @param position The position of the event.
	 */
	public PositionEvent(Position base, Position position) {
		this.base = base;
		this.position = position;
	}

	/**
	 * Creates the event with the specified base and position.
	 * @param base The position base.
	 * @param position The position of the event.
	 * @param offsetX The offset x.
	 * @param offsetY The offset y.
	 */
	public PositionEvent(Position base, Position position, int offsetX, int offsetY) {
		this(base, position);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	/**
	 * Gets the base.
	 * @return The base.
	 */
	public Position getBase() {
		return base;
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
	 * Gets the position.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}
}