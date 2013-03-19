package org.apollo.game.sync.block;

/**
 * An {@link SynchronizationBlock} that notifies players of a hit update.
 * @author Steve
 */
public final class SecondHitUpdateBlock extends HitUpdateBlock {

	/**
	 * Create a new hit block.
	 * @param damage The damage that is being done.
	 * @param current The current hitpoints.
	 * @param max The maximum hitpoints.
	 */
	SecondHitUpdateBlock(int damage, int current, int max) {
		super(damage, current, max);
	}

	/**
	 * Create a new hit block.
	 * @param damage The damage that is being done.
	 * @param current The current hitpoints.
	 * @param max The maximum hitpoints.
	 * @param type The hit type.
	 */
	SecondHitUpdateBlock(int damage, int current, int max, int type) {
		super(damage, current, max, type);
	}
}
