package org.apollo.game.event.impl;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An {@link Event} which updates the region.
 * @author Steve
 */
public final class RegionUpdateEvent extends Event {

	/**
	 * The base.
	 */
	private final Position base;

	/**
	 * The position.
	 */
	private final Position position;

	/**
	 * The events to send.
	 */
	private final List<Event> events;

	/**
	 * Creates a new region update event.
	 * @param base The base.
	 * @param position The region.
	 * @param event The event to send.
	 */
	public RegionUpdateEvent(Position base, Position position, Event event) {
		this(base, position, new ArrayList<Event>(1));
		events.add(event);
	}

	/**
	 * Creates a new region update event.
	 * @param base The base.
	 * @param position The region.
	 * @param events The events to send.
	 */
	public RegionUpdateEvent(Position base, Position position, List<Event> events) {
		this.base = base;
		this.position = position;
		this.events = events;
	}

	/**
	 * Gets the base.
	 * @return The base.
	 */
	public Position getBase() {
		return base;
	}

	/**
	 * Gets the events.
	 * @return The events.
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * Gets the position.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

}
