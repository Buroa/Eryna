package org.apollo.game.model.skill.farming;

import org.apollo.game.model.Player;

/**
 * The farming set.
 * @author Steve
 */
public final class FarmingSet {

	/**
	 * The allotment.
	 */
	private final Allotments allotment;

	/**
	 * The flowers.
	 */
	private final Flowers flowers;

	/**
	 * The compost.
	 */
	private final Compost compost;

	/**
	 * The herbs.
	 */
	private final Herbs herbs;

	/**
	 * The hops.
	 */
	private final Hops hops;

	/**
	 * The bushes.
	 */
	private final Bushes bushes;

	/**
	 * The special plant one.
	 */
	private final SpecialPlantOne specialPlantOne;

	/**
	 * The special plant two.
	 */
	private final SpecialPlantTwo specialPlantTwo;

	/**
	 * The seedling.
	 */
	private final Seedling seedling;

	/**
	 * The fruit tree.
	 */
	private final FruitTree fruitTree;

	/**
	 * Initializes the farming set.
	 * @param player The player.
	 */
	public FarmingSet(Player player) {
		allotment = new Allotments(player);
		flowers = new Flowers(player);
		compost = new Compost(player);
		herbs = new Herbs(player);
		hops = new Hops(player);
		bushes = new Bushes(player);
		specialPlantOne = new SpecialPlantOne(player);
		specialPlantTwo = new SpecialPlantTwo(player);
		seedling = new Seedling(player);
		fruitTree = new FruitTree(player);
	}

	/**
	 * Gets the allotment.
	 * @return The allotment.
	 */
	public Allotments getAllotment() {
		return allotment;
	}

	/**
	 * Gets the bushes.
	 * @return The bushes.
	 */
	public Bushes getBushes() {
		return bushes;
	}

	/**
	 * Gets the compost.
	 * @return The compost.
	 */
	public Compost getCompost() {
		return compost;
	}

	/**
	 * Gets the flowers.
	 * @return The flowers.
	 */
	public Flowers getFlowers() {
		return flowers;
	}

	/**
	 * Gets the fruit tree.
	 * @return The fruit tree.
	 */
	public FruitTree getFruitTrees() {
		return fruitTree;
	}

	/**
	 * Gets the herbs.
	 * @return The herbs.
	 */
	public Herbs getHerbs() {
		return herbs;
	}

	/**
	 * Gets the hops.
	 * @return The hops.
	 */
	public Hops getHops() {
		return hops;
	}

	/**
	 * Gets the seedling.
	 * @return The seedling.
	 */
	public Seedling getSeedling() {
		return seedling;
	}

	/**
	 * Gets the special plant one.
	 * @return The special plant one.
	 */
	public SpecialPlantOne getSpecialPlantOne() {
		return specialPlantOne;
	}

	/**
	 * Gets the special plant two.
	 * @return The special plant two.
	 */
	public SpecialPlantTwo getSpecialPlantTwo() {
		return specialPlantTwo;
	}

}
