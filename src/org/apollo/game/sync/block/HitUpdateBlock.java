package org.apollo.game.sync.block;

/**
 * An {@link SynchronizationBlock} that notifies players of a hit update.
 * @author Steve
 */
public class HitUpdateBlock extends SynchronizationBlock {

	/**
	 * The damage that is being done.
	 */
	private final int damage;

	/**
	 * The current hitpoints.
	 */
	private final int current;

	/**
	 * The maximum hitpoints.
	 */
	private final int max;

	/**
	 * The hit type.
	 */
	private final int type;

	/**
	 * Create a new hit block.
	 * @param damage The damage that is being done.
	 * @param current The current hitpoints.
	 * @param max The maximum hitpoints.
	 */
	HitUpdateBlock(int damage, int current, int max) {
		this.damage = damage;
		this.current = current;
		this.max = max;
		this.type = damage == 0 ? 0 : 1;
	}

	/**
	 * Create a new hit block.
	 * @param damage The damage that is being done.
	 * @param current The current hitpoints.
	 * @param max The maximum hitpoints.
	 * @param type The hit type.
	 */
	HitUpdateBlock(int damage, int current, int max, int type) {
		this.damage = damage;
		this.current = current;
		this.max = max;
		this.type = type;
	}

	/**
	 * Gets the current hitpoints.
	 * @return The current hitpoints.
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * Gets the damage.
	 * @return The damage that is being done.
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Gets the max hitpoints.
	 * @return The max hitpoints.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Gets the hit type.
	 * @return The hit type.
	 */
	public int getType() {
		return type;
	}
}