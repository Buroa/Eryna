package org.apollo.game.sync.block;

/**
 * The transform {@link SynchronizationBlock}.
 * @author Steve
 */
public final class TransformBlock extends SynchronizationBlock {

	/**
	 * The transform npc id.
	 */
	private final int npcId;

	/**
	 * Creates the transform block.
	 * @param npcId The transform npc id.
	 */
	TransformBlock(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * Gets the transform npc id.
	 * @return The npc transform id.
	 */
	public int getId() {
		return npcId;
	}
}
