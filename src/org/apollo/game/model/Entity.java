package org.apollo.game.model;

/**
 * A class which holds a entity linked to a position.
 * @author Steve
 */
public abstract class Entity {
	
	/**
	 * The player type.
	 */
	public static final int PLAYER_TYPE = 0;
	
	/**
	 * The npc type.
	 */
	public static final int NPC_TYPE = 1;
	
	/**
	 * The static objec type.
	 */
	public static final int STATIC_OBJECT_TYPE = 2;
	
	/**
	 * The dynamic object type.
	 */
	public static final int DYNAMIC_OBJECT_TYPE = 3;
	
	/**
	 * The ground type.
	 */
	public static final int GROUND_TYPE = 4;

	/**
	 * The position of the entity.
	 */
	protected final Position position;

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
	
	/**
	 * Publishes this entity.
	 */
	public final void publish() {
		World.getWorld().getRegionManager().getRegionByPosition(position).add(this);
	}
	
	/**
	 * Gets the type of entity.
	 * @return The type of entity.
	 */
	public abstract int type();

}
