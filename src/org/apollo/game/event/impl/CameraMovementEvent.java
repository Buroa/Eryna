package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} for when the player moves their camera around.
 * @author Steve
 */
public final class CameraMovementEvent extends Event {

	/**
	 * The camera x axis.
	 */
	private final int x;

	/**
	 * The camera y axis.
	 */
	private final int y;

	/**
	 * Creates the new camera movement event.
	 * @param x The camera x axis.
	 * @param y The camera y axis.
	 */
	public CameraMovementEvent(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the camera x axis.
	 * @return The camera x axis.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the camera y axis.
	 * @return The camera y axis.
	 */
	public int getY() {
		return y;
	}
}
