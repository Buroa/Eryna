package org.apollo.game.model.skill.farming;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 18/03/12 Time: 11:00 To change
 * this template use File | Settings | File
 * Templates.
 */
@SuppressWarnings("javadoc")
public final class Seedling {

	public enum SeedlingData {
		OAK(5312, 5358, 5364, 5370), WILLOW(5313, 5359, 5365, 5371), MAPLE(5314, 5360, 5366, 5372), YEW(5315, 5361,
				5367, 5373), MAGIC(5316, 5362, 5368, 5374), SPIRIT(5317, 5363, 5369, 5375), APPLE(5283, 5480, 5488,
				5496), BANANA(5284, 5481, 5489, 5497), ORANGE(5285, 5482, 5490, 5498), CURRY(5286, 5483, 5491, 5499), PINEAPPLE(
				5287, 5484, 5492, 5500), PAPAYA(5288, 5485, 5493, 5501), PALM(5289, 5486, 5494, 5502), CALQUAT(5290,
				5487, 5495, 5503);

		private int seedId;

		private int unwateredSeedlingId;

		private int wateredSeedlingId;

		private int saplingId;

		private static Map<Integer, SeedlingData> seeds = new HashMap<Integer, SeedlingData>();

		private static Map<Integer, SeedlingData> unwatered = new HashMap<Integer, SeedlingData>();

		private static Map<Integer, SeedlingData> watered = new HashMap<Integer, SeedlingData>();

		static {
			for (final SeedlingData data : SeedlingData.values()) {
				seeds.put(data.seedId, data);
				unwatered.put(data.unwateredSeedlingId, data);
				watered.put(data.wateredSeedlingId, data);
			}
		}

		public static SeedlingData getSeed(int seedId) {
			return seeds.get(seedId);
		}

		public static SeedlingData getUnwatered(int seedId) {
			return unwatered.get(seedId);
		}

		public static SeedlingData getWatered(int seedId) {
			return watered.get(seedId);
		}

		SeedlingData(int seedId, int unwateredSeedlingId, int wateredSeedlingId, int saplingId) {
			this.seedId = seedId;
			this.unwateredSeedlingId = unwateredSeedlingId;
			this.wateredSeedlingId = wateredSeedlingId;
			this.saplingId = saplingId;
		}

		public int getSaplingId() {
			return saplingId;
		}

		public int getSeedId() {
			return seedId;
		}

		public int getUnwateredSeedlingId() {
			return unwateredSeedlingId;
		}

		public int getWateredSeedlingId() {
			return wateredSeedlingId;
		}
	}

	private final Player player;

	public Seedling(Player player) {
		this.player = player;
	}

	public boolean fillPotWithSoil(int itemId, int objectX, int objectY) {
		if (itemId != 5350)
			return false;

		final FarmingSet farmingSet = player.getSettings().getFarmingSet();
		if (!(farmingSet.getAllotment().checkIfRaked(objectX, objectY) || farmingSet.getBushes().checkIfRaked(objectX, objectY)
				|| farmingSet.getFlowers().checkIfRaked(objectX, objectY)
				|| farmingSet.getFruitTrees().checkIfRaked(objectX, objectY)
				|| farmingSet.getHerbs().checkIfRaked(objectX, objectY) || farmingSet.getHops().checkIfRaked(objectX, objectY)
				|| farmingSet.getSpecialPlantOne().checkIfRaked(objectX, objectY) || farmingSet.getSpecialPlantTwo()
				.checkIfRaked(objectX, objectY))) {
			player.sendMessage("You can only fill your pot on raked patches.");
			return true;
		}

		if (!player.getInventory().contains(FarmingConstants.TROWEL)) {
			player.sendMessage("You need a gardening trowel to fill this pot with soil.");
			return true;
		}
		player.getInventory().remove(new Item(itemId));
		player.playAnimation(new Animation(FarmingConstants.FILLING_POT_ANIM));
		player.sendMessage("You fill the empty plant pot with soil.");
		player.getInventory().add(new Item(5354));
		return true;
	}

	public void makeSaplingInBank(int itemId) {
		final SeedlingData seedlingData = SeedlingData.getWatered(itemId);
		if (seedlingData == null)
			return;
		player.getBank().remove(new Item(itemId));
		player.getBank().add(new Item(seedlingData.getSaplingId()));
	}

	public void makeSaplingInInv(int itemId) {
		final SeedlingData seedlingData = SeedlingData.getWatered(itemId);
		if (seedlingData == null)
			return;
		player.getInventory().remove(new Item(itemId));
		player.getInventory().add(new Item(seedlingData.getSaplingId()));
	}

	public boolean placeSeedInPot(int itemUsed, int usedWith, int itemUsedSlot, int usedWithSlot) {
		SeedlingData seedlingData = SeedlingData.getSeed(itemUsed);
		if (seedlingData == null)
			seedlingData = SeedlingData.getUnwatered(usedWith);
		if (seedlingData == null || itemUsed != 5354 && usedWith != 5354)
			return false;
		if (player.getInventory().removeSlot(itemUsed == 5354 ? itemUsedSlot : usedWithSlot,
				new Item(seedlingData.getSeedId())) > 0)
			player.getInventory().set(itemUsed == 5354 ? itemUsedSlot : usedWithSlot,
					new Item(seedlingData.getUnwateredSeedlingId()));
		else if (player.getInventory().remove(seedlingData.getSeedId()))
			player.getInventory().add(new Item(seedlingData.getUnwateredSeedlingId()));
		player.sendMessage("You sow some maple tree seeds in the plantpots.");
		player.sendMessage("They need watering before they will grow.");
		return true;
	}

	public boolean waterSeedling(int itemUsed, int usedWith, int itemUsedSlot, int usedWithSlot) {
		SeedlingData seedlingData = SeedlingData.getUnwatered(itemUsed);
		if (seedlingData == null)
			seedlingData = SeedlingData.getUnwatered(usedWith);
		if (seedlingData == null
				|| !new Item(itemUsed).getDefinition().getName().toLowerCase().contains("watering") && !new Item(
						usedWith).getDefinition().getName().toLowerCase().contains("watering"))
			return false;
		if (itemUsed >= 5333 && itemUsed <= 5340)
			if (player.getInventory().remove(itemUsed))
				player.getInventory().add(new Item(itemUsed == 5333 ? itemUsed - 2 : itemUsed - 1));
		if (usedWith >= 5333 && usedWith <= 5340)
			if (player.getInventory().remove(usedWith))
				player.getInventory().add(new Item(itemUsed == 5333 ? itemUsed - 2 : itemUsed - 1));
		player.sendMessage("You water the "
				+ new Item(seedlingData.getSeedId()).getDefinition().getName().toLowerCase() + ".");
		player.getInventory().remove(new Item(seedlingData.getUnwateredSeedlingId()));
		player.getInventory().add(new Item(seedlingData.getWateredSeedlingId()));
		return true;

	}
}
