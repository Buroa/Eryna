package org.apollo.game.sync.block;

/**
 * The force chat {@link SynchronizationBlock}.
 * @author Steve
 */
public final class InteractingEntityBlock extends SynchronizationBlock {

	/**
	 * The entity id.
	 */
	private final int id;

	/**
	 * Creates the turn to entity block.
	 * @param id The entity to turn too.
	 */
	InteractingEntityBlock(int id) {
		this.id = id;
	}

	/**
	 * Gets the entity id.
	 * @return The entity id.
	 */
	public int getId() {
		return id;
	}
}
