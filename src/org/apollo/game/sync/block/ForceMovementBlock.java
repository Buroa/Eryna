package org.apollo.game.sync.block;

import org.apollo.game.model.Position;

/**
 * The force movement {@link SynchronizationBlock}.
 * @author Steve
 */
public final class ForceMovementBlock extends SynchronizationBlock {

	/**
	 * The position we are currently at.
	 */
	private final Position currentPosition;

	/**
	 * The position to walk too.
	 */
	private final Position position;

	/**
	 * The speed of the X coordinate.
	 */
	private final int firstSpeed;

	/**
	 * The speed of the Y coordinate.
	 */
	private final int secondSpeed;

	/**
	 * The direction.
	 */
	private final int direction;

	/**
	 * Creates the force movement block.
	 * @param currentPosition The position we are currently at.
	 * @param position The position to walk too.
	 * @param firstSpeed The speed of the X coordinate.
	 * @param secondSpeed The speed of the Y coordinate.
	 * @param direction The direction.
	 */
	ForceMovementBlock(Position currentPosition, Position position, int firstSpeed, int secondSpeed, int direction) {
		this.currentPosition = currentPosition;
		this.position = position;
		this.firstSpeed = firstSpeed;
		this.secondSpeed = secondSpeed;
		this.direction = direction;
	}

	/**
	 * Gets the position we are at.
	 * @return The position we are at.
	 */
	public Position getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Gets the direction.
	 * @return The direction.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Gets the speed of the X coordinate.
	 * @return The speed of the X coordinate.
	 */
	public int getFirstSpeed() {
		return firstSpeed;
	}

	/**
	 * Gets the position to walk too.
	 * @return The position to walk too.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the speed of the Y coordinate.
	 * @return The speed of the Y coordinate.
	 */
	public int getSecondSpeed() {
		return secondSpeed;
	}
}
