package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Position;

/**
 * An {@link Event} that displays a {@link Graphic} at a specific
 * {@link Position}
 * @author Steve
 */
public final class GraphicEvent extends MapEvent {

	/**
	 * The graphic.
	 */
	private final Graphic graphic;

	/**
	 * Creates the new graphic event.
	 * @param graphic The graphic.
	 * @param position The position of the graphic.
	 */
	public GraphicEvent(Graphic graphic, Position position) {
		super(position);
		this.graphic = graphic;
	}

	/**
	 * Gets the graphic.
	 * @return The graphic.
	 */
	public Graphic getGraphic() {
		return graphic;
	}

}
