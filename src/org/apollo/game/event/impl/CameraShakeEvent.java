package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} that makes the camera shake.
 * @author Steve
 */
public final class CameraShakeEvent extends Event {

	/**
	 * The maximum magnitudes.
	 */
	public final static int MAX_MAGNITUDES = 3;

	/**
	 * The camera shake type.
	 */
	private final int type;

	/**
	 * The magnitudes from [0]..[2]
	 */
	private final int[] magnitudes;

	/**
	 * Creates the camera shake event.
	 * @param type The shake type.
	 * @param magnitudes The magnitudes from [0]..[2]
	 * @throws Exception Invalid magnitudes length.
	 */
	public CameraShakeEvent(int type, int[] magnitudes) throws Exception {
		this.type = type;
		if (this.magnitudes.length == MAX_MAGNITUDES)
			this.magnitudes = magnitudes;
		else
			throw new Exception("Invalid length for magnitudes.");
	}

	/**
	 * Gets the magnitudes.
	 * @return The magnitudes from [0]..[2]
	 */
	public int[] getMagnitudes() {
		return magnitudes;
	}

	/**
	 * Gets the camera shake type.
	 * @return The camera shake type.
	 */
	public int getType() {
		return type;
	}
}
