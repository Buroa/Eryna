package org.apollo.game.model;

/**
 * A class which holds a entity linked to a position.
 * @author Steve
 */
public abstract class Entity {

	/**
	 * The position of the entity.
	 */
	private final Position position;

	/**
	 * Creates a new entity.
	 * @param position The position of the entity.
	 */
	public Entity(Position position) {
		this.position = position;
	}

	/**
	 * Gets the position of the entity.
	 * @return The position of the entity.
	 */
	public Position getPosition() {
		return position;
	}

}
