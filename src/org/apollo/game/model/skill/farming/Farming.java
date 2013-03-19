package org.apollo.game.model.skill.farming;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;

@SuppressWarnings("javadoc")
public final class Farming {

	public static boolean harvest(Player player, int x, int y) {
		// Okay their farming, lets start a new task.
		if (!player.getSettings().isFarming())
			player.startFarming();
		// allotments

		final FarmingSet farmingSet = player.getSettings().getFarmingSet();

		if (farmingSet.getAllotment().harvest(x, y))
			return true;
		// flowers
		if (farmingSet.getFlowers().harvest(x, y))
			return true;
		// herbs
		if (farmingSet.getHerbs().harvest(x, y))
			return true;
		// hops
		if (farmingSet.getHops().harvest(x, y))
			return true;
		// bushes
		if (farmingSet.getBushes().harvestOrCheckHealth(x, y))
			return true;
		/*
		 * // trees if (player.getTrees().checkHealth(x, y)) { return true; } if
		 * (player.getTrees().cut(x, y)) { return
		 * true; }
		 */
		// fruit trees
		if (farmingSet.getFruitTrees().harvestOrCheckHealth(x, y))
			return true;
		// special plant one
		if (farmingSet.getSpecialPlantOne().harvestOrCheckHealth(x, y))
			return true;
		// special plant two
		if (farmingSet.getSpecialPlantTwo().harvestOrCheckHealth(x, y))
			return true;
		return false;
	}

	public static boolean inspectObject(Player player, int x, int y) {
		if (!player.getSettings().isFarming())
			player.startFarming();

		final FarmingSet farmingSet = player.getSettings().getFarmingSet();
		// allotments
		if (farmingSet.getAllotment().inspect(x, y))
			return true;
		if (farmingSet.getFlowers().inspect(x, y))
			return true;
		// herbs
		if (farmingSet.getHerbs().inspect(x, y))
			return true;
		// hops
		if (farmingSet.getHops().inspect(x, y))
			return true;
		// bushes
		if (farmingSet.getBushes().inspect(x, y))
			return true;
		/*
		 * // trees if (player.getTrees().inspect(x, y)) { return true; }
		 */
		// fruit trees
		if (farmingSet.getFruitTrees().inspect(x, y))
			return true;
		// special plant one
		if (farmingSet.getSpecialPlantOne().inspect(x, y))
			return true;
		// special plant two
		if (farmingSet.getSpecialPlantTwo().inspect(x, y))
			return true;
		return false;
	}

	/*
	 * public static boolean guide(Player player, int x, int y) { // allotments
	 * if (player.getAllotment().guide(x, y)) {
	 * return true; } // flowers if (player.getFlowers().guide(x, y)) { return
	 * true; } // herbs if
	 * (player.getHerbs().guide(x, y)) { return true; } // hops if
	 * (player.getHops().guide(x, y)) { return true; } //
	 * bushes if (player.getBushes().guide(x, y)) { return true; } // trees if
	 * (player.getTrees().guide(x, y)) { return
	 * true; } // fruit trees if (player.getFruitTrees().guide(x, y)) { return
	 * true; } // special plant one if
	 * (player.getSpecialPlantOne().guide(x, y)) { return true; } // special
	 * plant two if
	 * (player.getSpecialPlantTwo().guide(x, y)) { return true; } return false;
	 * }
	 */

	public static boolean prepareCrop(Player player, int item, int id, int x, int y) {
		if (!player.getSettings().isFarming())
			player.startFarming();

		final FarmingSet farmingSet = player.getSettings().getFarmingSet();
		// plant pot
		if (farmingSet.getSeedling().fillPotWithSoil(item, x, y))
			return true;
		// allotments
		if (farmingSet.getAllotment().curePlant(x, y, item))
			return true;
		if (farmingSet.getAllotment().putCompost(x, y, item))
			return true;
		if (farmingSet.getAllotment().clearPatch(x, y, item))
			return true;
		if (item >= 3422 && item <= 3428 && id == 4090) {
			player.getInventory().remove(item);
			player.getInventory().add(item + 8);
			player.playAnimation(new Animation(832));
			player.sendMessage("You put the olive oil on the fire, and turn it into sacred oil.");
			return true;
		}
		if (item <= 5340 && item > 5332)
			if (farmingSet.getAllotment().waterPatch(x, y, item))
				return true;
		if (farmingSet.getAllotment().plantSeed(x, y, item))
			return true;

		// flowers
		if (farmingSet.getFlowers().plantScareCrow(x, y, item))
			return true;
		if (farmingSet.getFlowers().curePlant(x, y, item))
			return true;
		if (farmingSet.getFlowers().putCompost(x, y, item))
			return true;
		if (farmingSet.getFlowers().clearPatch(x, y, item))
			return true;
		if (item <= 5340 && item > 5332)
			if (farmingSet.getFlowers().waterPatch(x, y, item))
				return true;
		if (farmingSet.getFlowers().plantSeed(x, y, item))
			return true;
		if (farmingSet.getCompost().handleItemOnObject(item, id, x, y))
			return true;
		// herbs
		if (farmingSet.getHerbs().curePlant(x, y, item))
			return true;
		if (farmingSet.getHerbs().putCompost(x, y, item))
			return true;
		if (farmingSet.getHerbs().clearPatch(x, y, item))
			return true;
		if (farmingSet.getHerbs().plantSeed(x, y, item))
			return true;
		// hops
		if (farmingSet.getHops().curePlant(x, y, item))
			return true;
		if (farmingSet.getHops().putCompost(x, y, item))
			return true;
		if (farmingSet.getHops().clearPatch(x, y, item))
			return true;
		if (item <= 5340 && item > 5332)
			if (farmingSet.getHops().waterPatch(x, y, item))
				return true;
		if (farmingSet.getHops().plantSeed(x, y, item))
			return true;
		// bushes
		if (farmingSet.getBushes().curePlant(x, y, item))
			return true;
		if (farmingSet.getBushes().putCompost(x, y, item))
			return true;

		if (farmingSet.getBushes().clearPatch(x, y, item))
			return true;
		if (farmingSet.getBushes().plantSeed(x, y, item))
			return true;
		/*
		 * // trees if (player.getTrees().pruneArea(x, y, item)) { return true;
		 * } if (player.getTrees().putCompost(x, y,
		 * item)) { return true; } if (player.getTrees().plantSapling(x, y,
		 * item)) { return true; } if
		 * (player.getTrees().clearPatch(x, y, item)) { return true; }
		 */
		// fruit trees
		if (farmingSet.getFruitTrees().pruneArea(x, y, item))
			return true;
		if (farmingSet.getFruitTrees().putCompost(x, y, item))
			return true;
		if (farmingSet.getFruitTrees().clearPatch(x, y, item))
			return true;
		if (farmingSet.getFruitTrees().plantSapling(x, y, item))
			return true;
		// special plant one
		if (farmingSet.getSpecialPlantOne().curePlant(x, y, item))
			return true;
		if (farmingSet.getSpecialPlantOne().putCompost(x, y, item))
			return true;
		if (farmingSet.getSpecialPlantOne().clearPatch(x, y, item))
			return true;
		if (farmingSet.getSpecialPlantOne().plantSapling(x, y, item))
			return true;
		// Special plant two
		if (farmingSet.getSpecialPlantTwo().curePlant(x, y, item))
			return true;
		if (farmingSet.getSpecialPlantTwo().putCompost(x, y, item))
			return true;
		if (farmingSet.getSpecialPlantTwo().clearPatch(x, y, item))
			return true;
		if (farmingSet.getSpecialPlantTwo().plantSeeds(x, y, item))
			return true;
		return false;
	}

	public static void watch(Player player, int farm, int index) {
		if (!player.getSettings().isFarming())
			player.startFarming();

		final FarmingSet farmingSet = player.getSettings().getFarmingSet();
		switch (farm) {
		case 0:
			switch (index) {
			case 1:
				farmingSet.getAllotment().setFarmingWatched(0, true);
				farmingSet.getAllotment().setFarmingWatched(1, true);
				return;
			case 2:
				farmingSet.getAllotment().setFarmingWatched(2, true);
				farmingSet.getAllotment().setFarmingWatched(3, true);
				return;
			case 3:
				farmingSet.getAllotment().setFarmingWatched(4, true);
				farmingSet.getAllotment().setFarmingWatched(5, true);
			case 4:
				farmingSet.getAllotment().setFarmingWatched(6, true);
				farmingSet.getAllotment().setFarmingWatched(7, true);
			}
			return;
		}
	}
}
