package org.apollo.game.model;

import org.apollo.util.NameUtil;

/**
 * Represents a single frenemy.
 * @author Steve
 */
public final class Frenemy {

	/**
	 * The frenemies name.
	 */
	private final String frenemy;

	/**
	 * The encoded frenemies name.
	 */
	private final long encodedFrenemy;

	/**
	 * Flag for if the {@link frenemy} is a friend.
	 */
	private final boolean friend;

	/**
	 * Creates a new frenemy.
	 * @param frenemy The frenemies name.
	 * @param friend True if friend, false if enemy.
	 */
	public Frenemy(long frenemy, boolean friend) {
		this.frenemy = NameUtil.decodeBase37(frenemy);
		this.encodedFrenemy = frenemy;
		this.friend = friend;
	}

	/**
	 * Creates a new frenemy.
	 * @param frenemy The frenemies name.
	 * @param friend True if friend, false if enemy.
	 */
	public Frenemy(String frenemy, boolean friend) {
		this.frenemy = frenemy;
		this.encodedFrenemy = NameUtil.encodeBase37(frenemy);
		this.friend = friend;
	}

	/**
	 * Gets the encoded frenemies name.
	 * @return The encoded frenemies name.
	 */
	public long getEncodedFrenemy() {
		return encodedFrenemy;
	}

	/**
	 * Gets the frenemies name.
	 * @return The frenemies name.
	 */
	public String getFrenemy() {
		return frenemy;
	}

	/**
	 * Returns the enemy flag.
	 * @return True if enemy, false if not.
	 */
	public boolean isEnemy() {
		return friend == false;
	}

	/**
	 * Returns the friend flag.
	 * @return True if friend, false if not.
	 */
	public boolean isFriend() {
		return friend == true;
	}

	@Override
	public String toString() {
		return Frenemy.class.getName() + " [name=" + frenemy + ", friend=" + isFriend() + "]";
	}
}
