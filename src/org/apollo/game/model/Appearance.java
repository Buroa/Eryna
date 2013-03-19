package org.apollo.game.model;

/**
 * Represents the appearance of a player.
 * @author Graham
 */
public final class Appearance {

	/**
	 * The npc id.
	 */
	private int npcId = -1;

	/**
	 * The default appearance.
	 */
	public static final Appearance DEFAULT_APPEARANCE = new Appearance(Gender.MALE, new int[] { 0, 10, 18, 26, 33, 36,
			42 }, new int[5]);

	/**
	 * The player's gender.
	 */
	private final Gender gender;

	/**
	 * The array of clothing/characteristic styles.
	 */
	private final int[] style;

	/**
	 * The array of clothing/skin colours.
	 */
	private final int[] colors;

	/**
	 * The skull icon.
	 */
	private byte skullIcon = 0;

	/**
	 * The run animation.
	 */
	private int runAnimation = -1;

	/**
	 * The walk animation.
	 */
	private int walkAnimation = -1;

	/**
	 * The stand animation.
	 */
	private int standAnimation = -1;

	/**
	 * Creates the appearance with the specified gender, style and colors.
	 * @param gender The gender.
	 * @param style The style.
	 * @param colors The colors.
	 */
	public Appearance(Gender gender, int[] style, int[] colors) {
		if (gender == null || style == null || colors == null)
			throw new NullPointerException();
		if (style.length != 7)
			throw new IllegalArgumentException("the style array must have 7 elements");
		if (colors.length != 5)
			throw new IllegalArgumentException("the colors array must have 5 elements");
		this.gender = gender;
		this.style = style;
		this.colors = colors;
	}

	/**
	 * Gets the player's colors.
	 * @return The player's colors.
	 */
	public int[] getColors() {
		return colors;
	}

	/**
	 * Gets the gender of the player.
	 * @return The gender of the player.
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * Gets the npc id.
	 * @return The npc id.
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * Gets the run animation.
	 * @return The run animation.
	 */
	public int getRunAnimation() {
		return runAnimation == -1 ? 0x338 : runAnimation;
	}

	/**
	 * Gets the skull icon.
	 * @return The skull icon.
	 */
	public byte getSkull() {
		return skullIcon;
	}

	/**
	 * Gets the stand animation.
	 * @return The stand animation.
	 */
	public int getStandAnimation() {
		return standAnimation == -1 ? 0x328 : standAnimation;
	}

	/**
	 * Gets the player's styles.
	 * @return The player's styles.
	 */
	public int[] getStyle() {
		/*
		 * Info on the elements of the array itself: 0 = head 1 = chin/beard 2 =
		 * chest 3 = arms 4 = hands 5 = legs 6 =
		 * feet
		 */
		return style;
	}

	/**
	 * Gets the walk animation.
	 * @return The walk animation.
	 */
	public int getWalkAnimation() {
		return walkAnimation == -1 ? 0x333 : walkAnimation;
	}

	/**
	 * Checks if the player is female.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isFemale() {
		return gender == Gender.FEMALE;
	}

	/**
	 * Checks if the player is male.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isMale() {
		return gender == Gender.MALE;
	}

	/**
	 * Sets the npc id.
	 * @param npcId The npc id.
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * Sets the run animation.
	 * @param runAnimation The run animation.
	 */
	public void setRunAnimation(int runAnimation) {
		this.runAnimation = runAnimation;
	}

	/**
	 * Sets the skull icon.
	 * @param skullIcon The skull icon.
	 */
	public void setSkullIcon(byte skullIcon) {
		this.skullIcon = skullIcon;
	}

	/**
	 * Sets the stand animation.
	 * @param standAnimation The stand animation.
	 */
	public void setStandAnimation(int standAnimation) {
		this.standAnimation = standAnimation;
	}

	/**
	 * Sets the walk animation.
	 * @param walkAnimation The walk animation.
	 */
	public void setWalkAnimation(int walkAnimation) {
		this.walkAnimation = walkAnimation;
	}
}
