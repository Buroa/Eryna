package org.apollo.game.model;

import org.apollo.game.event.impl.SendFriendEvent;
import org.apollo.game.event.impl.SendIgnoreEvent;

/**
 * Represents a set of the character's frenemies.
 * @author Steve
 */
public final class FrenemySet {

	/**
	 * The number of frenemies.
	 */
	private static final int FRENEMIE_COUNT = 100;

	/**
	 * The character.
	 */
	private final Character character;

	/**
	 * The frenemies.
	 */
	private final Frenemy[] frenemies = new Frenemy[FRENEMIE_COUNT];

	/**
	 * Sets the frenemy set.
	 * @param character The character.
	 */
	public FrenemySet(Character character) {
		this.character = character;
	}

	/**
	 * Adds a frenemy.
	 * @param frenemy The frenemy to add.
	 * @return True if the frenemy was added, false if otherwise.
	 */
	public boolean add(Frenemy frenemy) {
		for (int slot = 0; slot < FRENEMIE_COUNT; slot++) {
			final Frenemy other = frenemies[slot];
			if (other != null && other.getFrenemy().equals(frenemy.getFrenemy()))
				return false;
		}
		for (int slot = 0; slot < FRENEMIE_COUNT; slot++) {
			final Frenemy other = frenemies[slot];
			if (other == null) {
				frenemies[slot] = frenemy;
				notifyFrenemieUpdated(frenemy);
				return true;
			}
		}
		return false;
	}

	/**
	 * Forces this frenemy set to refresh.
	 */
	public void forceRefresh() {
		if (character.isActive())
			notifyFrenemiesUpdated();
	}

	/**
	 * Gets the enemies.
	 * @return The enemies.
	 */
	public Frenemy[] getEnemies() {
		int _enemies = 0;
		final Frenemy[] enemies = new Frenemy[getEnemiesCount()];
		for (int i = 0; i < FRENEMIE_COUNT; i++) {
			final Frenemy frenemy = frenemies[i];
			if (frenemy != null && frenemy.isEnemy())
				enemies[_enemies++] = frenemy;
		}
		return enemies;
	}

	/**
	 * Gets the enemies count.
	 * @return The enemies count.
	 */
	public int getEnemiesCount() {
		int enemies = 0;
		for (final Frenemy frenemie : frenemies)
			if (frenemie != null && frenemie.isEnemy())
				enemies++;
		return enemies;
	}

	/**
	 * Gets the frenemy at the slot.
	 * @param i The slot.
	 * @return The frenemy at the slot i.
	 */
	public Frenemy getFrenemy(int i) {
		return frenemies[i];
	}

	/**
	 * Gets the friends.
	 * @return The friends.
	 */
	public Frenemy[] getFriends() {
		int _friends = 0;
		final Frenemy[] friends = new Frenemy[getFriendsCount()];
		for (int i = 0; i < FRENEMIE_COUNT; i++) {
			final Frenemy frenemy = frenemies[i];
			if (frenemy != null && frenemy.isFriend())
				friends[_friends++] = frenemy;
		}
		return friends;
	}

	/**
	 * Gets the friends count.
	 * @return The friends count.
	 */
	public int getFriendsCount() {
		int friends = 0;
		for (final Frenemy frenemie : frenemies)
			if (frenemie != null && frenemie.isFriend())
				friends++;
		return friends;
	}

	/**
	 * Checks if the string is a enemy of ours.
	 * @param checkAgainst The string to check against.
	 * @return True if our set contains the enemy, false if otherwise.
	 */
	public boolean isEnemy(String checkAgainst) {
		for (final Frenemy frenemy : getEnemies())
			if (frenemy.getFrenemy().equalsIgnoreCase(checkAgainst))
				return true;
		return false;
	}

	/**
	 * Checks if the string is a friend of ours.
	 * @param checkAgainst The string to check against.
	 * @return True if our set contains the friend, false if otherwise.
	 */
	public boolean isFriend(String checkAgainst) {
		for (final Frenemy frenemy : getFriends())
			if (frenemy.getFrenemy().equalsIgnoreCase(checkAgainst))
				return true;
		return false;
	}

	/**
	 * Notifies the character that the frenemies list has been updated.
	 */
	private void notifyFrenemiesUpdated() {
		boolean updateEnemies = false;
		final long[] enemies = new long[getEnemiesCount()];
		int _enemies = 0;
		for (int i = 0; i < FRENEMIE_COUNT; i++) {
			final Frenemy frenemie = frenemies[i];
			if (frenemie != null) {
				notifyFrenemieUpdated(frenemie);
				if (frenemie.isEnemy()) {
					updateEnemies = true;
					enemies[_enemies++] = frenemie.getEncodedFrenemy();
				}
			}
		}
		if (updateEnemies)
			character.send(new SendIgnoreEvent(enemies));
	}

	/**
	 * Notifies the character about a frenemy that needs updation.
	 * @param frenemy The frenemy to update.
	 */
	private void notifyFrenemieUpdated(Frenemy frenemy) {
		if (frenemy.isFriend() && character.isActive()) {
			final boolean online = World.getWorld().isPlayerOnline(frenemy.getFrenemy());
			character.send(new SendFriendEvent(frenemy.getEncodedFrenemy(), online ? 10 : 0));
		}
	}

	/**
	 * Removes a frenemy.
	 * @param frenemy The frenemy to remove.
	 * @return True if removed, false if otherwise.
	 */
	public boolean remove(Frenemy frenemy) {
		for (int slot = 0; slot < FRENEMIE_COUNT; slot++) {
			final Frenemy other = frenemies[slot];
			if (other != null && other.getFrenemy().equalsIgnoreCase(frenemy.getFrenemy())) {
				frenemies[slot] = null;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the size of the frenemies.
	 * @return The size of the frenemies.
	 */
	public int size() {
		return getFriendsCount() + getEnemiesCount();
	}

}
