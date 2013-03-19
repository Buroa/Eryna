package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Palette;
import org.apollo.game.model.Position;

/**
 * An {@link Event} that sends a dynamic map region that is constructed by using
 * groups of 8*8 tiles.
 * @author Steve
 */
public final class ConstructMapRegionEvent extends Event {

	/**
	 * The current position.
	 */
	private final Position position;

	/**
	 * The absolute palette.
	 */
	private final Palette palette;

	/**
	 * Creates the new construct map region event.
	 * @param position The current position.
	 * @param palette The palette data.
	 */
	public ConstructMapRegionEvent(Position position, Palette palette) {
		this.position = position;
		this.palette = palette;
	}

	/**
	 * Gets the palette.
	 * @return The palette.
	 */
	public Palette getPalette() {
		return palette;
	}

	/**
	 * Gets the position.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}
}
