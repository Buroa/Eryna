package org.apollo.game.model.region;

/**
 * Holds the music cache.
 * @author Steve
 */
public final class Music {

	/**
	 * The music id.
	 */
	private final int music;

	/**
	 * The coordinates south-west y.
	 */
	private final int swX;

	/**
	 * The coordinates south-west x.
	 */
	private final int swY;

	/**
	 * The coordinates south-easy x.
	 */
	private final int neX;

	/**
	 * The coordinates south-easy y.
	 */
	private final int neY;

	/**
	 * Creates a new cache for music.
	 * @param music The music id.
	 * @param swX The south-west x.
	 * @param swY The south-west y.
	 * @param neX The north-easx x.
	 * @param neY The north-east y.
	 */
	public Music(int music, int swX, int swY, int neX, int neY) {
		this.music = music;
		this.swX = swX;
		this.swY = swY;
		this.neX = neX;
		this.neY = neY;
	}

	/**
	 * Gets the music id.
	 * @return The music id.
	 */
	public int getMusic() {
		return music;
	}

	/**
	 * Gets the north-east x.
	 * @return The north-east x.
	 */
	public int getNeX() {
		return neX;
	}

	/**
	 * Gets the north-east y.
	 * @return The north-east y.
	 */
	public int getNeY() {
		return neY;
	}

	/**
	 * Gets the south-west x.
	 * @return The south-west x.
	 */
	public int getSwX() {
		return swX;
	}

	/**
	 * Gets the south-west y.
	 * @return The south-west y.
	 */
	public int getSwY() {
		return swY;
	}

}
