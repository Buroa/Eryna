package org.apollo.game.model.skill.farming;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.action.Action;
import org.apollo.game.event.impl.ConfigEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.TextUtil;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 24/02/12 Time: 20:34 To change
 * this template use File | Settings | File
 * Templates.
 */
@SuppressWarnings("javadoc")
public final class Flowers { // todo scarecrow 6059

	public enum FlowerData {

		MARIGOLD(5096, 6010, 2, 20, 0.35, 8.5, 47, 0x08, 0x0c), ROSEMARY(5097, 6014, 11, 20, 0.32, 12, 66.5, 0x0d, 0x11), NASTURTIUM(
				5098, 6012, 24, 20, 0.30, 19.5, 111, 0x12, 0x16), WOAD(5099, 1793, 25, 20, 0.27, 20.5, 115.5, 0x17,
				0x1b), LIMPWURT(5100, 225, 26, 25, 21.5, 8.5, 120, 0x1c, 0x20), ;

		public static FlowerData forId(int seedId) {
			return seeds.get(seedId);
		}

		private int seedId;

		private int harvestId;

		private int levelRequired;

		private int growthTime;

		private double diseaseChance;

		private double plantingXp;

		private double harvestXp;

		private int startingState;

		private int endingState;

		private static Map<Integer, FlowerData> seeds = new HashMap<Integer, FlowerData>();

		static {
			for (final FlowerData data : FlowerData.values())
				seeds.put(data.seedId, data);
		}

		FlowerData(int seedId, int harvestId, int levelRequired, int growthTime, double diseaseChance,
				double plantingXp, double harvestXp, int startingState, int endingState) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.levelRequired = levelRequired;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
		}

		public double getDiseaseChance() {
			return diseaseChance;
		}

		public int getEndingState() {
			return endingState;
		}

		public int getGrowthTime() {
			return growthTime;
		}

		public int getHarvestId() {
			return harvestId;
		}

		public double getHarvestXp() {
			return harvestXp;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getPlantingXp() {
			return plantingXp;
		}

		public int getSeedId() {
			return seedId;
		}

		public int getStartingState() {
			return startingState;
		}
	}

	// set of global constants for Farming

	public enum FlowerFieldsData {
		ARDOUGNE(0, new Position[] { new Position(2666, 3374), new Position(2667, 3375) }), PHASMATYS(1,
				new Position[] { new Position(3601, 3525), new Position(3602, 3526) }), FALADOR(2, new Position[] {
				new Position(3054, 3307), new Position(3055, 3308) }), CATHERBY(3, new Position[] {
				new Position(2809, 3463), new Position(2810, 3464) });

		public static FlowerFieldsData forIdPosition(Position position) {
			for (final FlowerFieldsData flowerFieldsData : FlowerFieldsData.values())
				if (FarmingConstants.inRangeArea(flowerFieldsData.getFlowerPosition()[0],
						flowerFieldsData.getFlowerPosition()[1], position))
					return flowerFieldsData;
			return null;
		}

		private int flowerIndex;

		private Position[] flowerPosition;

		FlowerFieldsData(int flowerIndex, Position[] flowerPosition) {
			this.flowerIndex = flowerIndex;
			this.flowerPosition = flowerPosition;
		}

		public int getFlowerIndex() {
			return flowerIndex;
		}

		public Position[] getFlowerPosition() {
			return flowerPosition;
		}
	}

	public enum InspectData {

		MARIGOLD(5096, new String[][] {
				{ "The seeds have only just been planted." },
				{ "The marigold plants have developed leaves." },
				{ "The marigold plants have begun to grow their", "flowers. The new flowers are orange and small at",
						"first." }, { "The marigold plants are larger, and more", "developed in their petals." },
				{ "The marigold plants are ready to harvest. Their", "flowers are fully matured." } }), ROSEMARY(5097,
				new String[][] { { "The seeds have only just been planted." },
						{ "The rosemary plant is taller than before." },
						{ "The rosemary plant is bushier and taller than", "before." },
						{ "The rosemary plant is developing a flower bud at", "its top." },
						{ "The plant is ready to harvest. The rosemary", "plant's flower has opened." } }), NASTURTIUM(
				5098, new String[][] {
						{ "The nasturtium seed has only just been planted." },
						{ "The nasturtium plants have started to develop", "leaves." },
						{ "The nasturtium plants have grown more leaves,", "and nine flower buds." },
						{ "The nasturtium plants open their flower buds." },
						{ "The plants are ready to harvest. The nasturtium",
								"plants grow larger than before and the flowers", "fully open." } }), WOAD(5099,
				new String[][] {
						{ "The woad seed has only just been planted." },
						{ "The woad plant produces more stalks, that split", "in tow near the top." },
						{ "The woad plant grows more segments from its", "intitial stalks." },
						{ "The woad plant develops flower buds on the end", "of each of its stalks." },
						{ "The woad plant is ready to harvest. The plant has",
								"all of its stalks pointing directly up, with", "all flowers open." } }), LIMPWURT(
				5100, new String[][] {
						{ "The seed has only just been planted." },
						{ "The limpwurt plant produces more roots." },
						{ "The limpwurt plant produces an unopened pink", "flower bud and continues to grow larger." },
						{ "The limpwurt plant grows larger, with more loops",
								"in its roots. The flower bud is still unopened." },
						{ "The limpwurt plant is ready to harvest. The",
								"flower finally opens wide, with a spike in the", "middle." } });
		private int seedId;

		private String[][] messages;

		private static Map<Integer, InspectData> seeds = new HashMap<Integer, InspectData>();

		static {
			for (final InspectData data : InspectData.values())
				seeds.put(data.seedId, data);
		}

		public static InspectData forId(int seedId) {
			return seeds.get(seedId);
		}

		InspectData(int seedId, String[][] messages) {
			this.seedId = seedId;
			this.messages = messages;
		}

		public String[][] getMessages() {
			return messages;
		}

		public int getSeedId() {
			return seedId;
		}
	}

	private final Player player;

	private static final double WATERING_CHANCE = 0.5;

	private static final double COMPOST_CHANCE = 0.9;

	private static final double SUPERCOMPOST_CHANCE = 0.7;

	private static final double CLEARING_EXPERIENCE = 4;

	public static final int SCARECROW = 6059;

	// Farming data
	public int[] farmingStages = new int[4];

	public int[] farmingSeeds = new int[4];

	public int[] farmingState = new int[4];

	public long[] farmingTimer = new long[4];

	/* set of the constants for the patch */

	public double[] diseaseChance = { 1, 1, 1, 1 };

	public boolean[] hasFullyGrown = { false, false, false, false };

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;

	public static final int WATERED = 0x01;

	public static final int DISEASED = 0x02;

	/* This is the enum holding the seeds info */

	public static final int DEAD = 0x03;

	/* This is the enum data about the different patches */

	public static final int FLOWER_PATCH_CONFIGS = 508;

	/* This is the enum that hold the different data for inspecting the plant */

	public Flowers(Player player) {
		this.player = player;
	}

	/* update all the patch states */

	public boolean checkIfRaked(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null)
			return false;
		if (farmingStages[flowerFieldsData.getFlowerIndex()] == 3)
			return true;
		return false;
	}

	/* getting the different config values */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (flowerFieldsData == null || itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)
			return false;
		if (farmingStages[flowerFieldsData.getFlowerIndex()] == 3)
			return true;
		if (farmingStages[flowerFieldsData.getFlowerIndex()] <= 3) {
			if (!player.getInventory().contains(FarmingConstants.RAKE)) {
				player.getInterfaceSet().sendStatement(null, "You need a rake to clear this path.");
				return true;
			}
			else {
				finalAnimation = FarmingConstants.RAKING_ANIM;
				finalDelay = 5;
			}
		} else if (!player.getInventory().contains(FarmingConstants.SPADE)) {
			player.getInterfaceSet().sendStatement(null, "You need a spade to clear this path.");
			return true;
		}
		else {
			finalAnimation = FarmingConstants.SPADE_ANIM;
			finalDelay = 3;
		}
		final int animation = finalAnimation;
		player.playAnimation(new Animation(animation));
		World.getWorld().schedule(new ScheduledTask(finalDelay, false) {

			@Override
			public void execute() {
				player.playAnimation(new Animation(animation));
				if (farmingStages[flowerFieldsData.getFlowerIndex()] <= 2) {
					farmingStages[flowerFieldsData.getFlowerIndex()]++;
					player.getInventory().add(new Item(6055));
				}
				else {
					farmingStages[flowerFieldsData.getFlowerIndex()] = 3;
					stop();
				}
				player.getSkillSet().addExperience(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[flowerFieldsData.getFlowerIndex()] = World.getWorld().getUptime();
				updateFlowerStates();
				if (farmingStages[flowerFieldsData.getFlowerIndex()] == 3) {
					stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetFlowers(flowerFieldsData.getFlowerIndex());
				player.sendMessage("You clear the patch.");
				player.stopAnimation();
				super.stop();
			}

		});
		return true;

	}

	/* getting the plant states */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null || itemId != 6036)
			return false;
		final FlowerData flowerData = FlowerData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null)
			return false;
		if (farmingState[flowerFieldsData.getFlowerIndex()] != 2) {
			player.sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().remove(new Item(itemId));
		player.getInventory().add(new Item(229));
		player.playAnimation(new Animation(FarmingConstants.CURING_ANIM));
		farmingState[flowerFieldsData.getFlowerIndex()] = 0;
		World.getWorld().schedule(new ScheduledTask(7, false) {

			@Override
			public void execute() {
				player.sendMessage("You cure the plant with a plant cure.");
				stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.stopAction();
				super.stop();
			}
		});

		return true;

	}

	/* calculating the disease chance and making the plant grow */

	@SuppressWarnings("unused")
	private void displayAll() {
		for (int i = 0; i < farmingStages.length; i++) {
			System.out.println("index : " + i);
			System.out.println("state : " + farmingState[i]);
			System.out.println("seeds : " + farmingSeeds[i]);
			System.out.println("level : " + farmingStages[i]);
			System.out.println("timer : " + farmingTimer[i]);
			System.out.println("disease chance : " + diseaseChance[i]);
			System.out.println("-----------------------------------------------------------------");
		}
	}

	/* calculations about the diseasing chance */

	public void doCalculations() {
		for (int i = 0; i < farmingSeeds.length; i++) {
			if (farmingStages[i] > 0 && farmingStages[i] <= 3 && World.getWorld().getUptime() - farmingTimer[i] >= 5) {
				farmingStages[i]--;
				farmingTimer[i] = World.getWorld().getUptime();
				updateFlowerStates();
			}
			if (World.getWorld().getUptime() - farmingTimer[i] >= 5 && farmingSeeds[i] > 0x21
					&& farmingSeeds[i] <= 0x24) {
				farmingSeeds[i]--;
				updateFlowerStates();
				return;
			}
			final FlowerData flowerData = FlowerData.forId(farmingSeeds[i]);
			if (flowerData == null)
				continue;

			final long difference = World.getWorld().getUptime() - farmingTimer[i];
			final long growth = flowerData.getGrowthTime();
			final int nbStates = flowerData.getEndingState() - flowerData.getStartingState();
			final int state = (int) (difference * nbStates / growth);
			if (farmingState[i] == 3 || farmingSeeds[i] == 0x21 || farmingTimer[i] == 0 || state > nbStates)
				continue;

			if (4 + state != farmingStages[i]) {
				farmingStages[i] = 4 + state;
				doStateCalculation(i);
				updateFlowerStates();
			}
		}
	}

	/* watering the patch */

	public void doStateCalculation(int index) {
		if (farmingState[index] == 3)
			return;
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 2)
			farmingState[index] = 3;

		if (farmingState[index] == 1 || farmingState[index] == 5 && farmingStages[index] != 3) {
			diseaseChance[index] *= 2;
			farmingState[index] = 0;
		}
		if (farmingState[index] == 0 && farmingStages[index] >= 5 && !hasFullyGrown[index]) {
			final FlowerData flowerData = FlowerData.forId(farmingSeeds[index]);
			if (flowerData == null)
				return;
			final double chance = diseaseChance[index] * flowerData.getDiseaseChance();
			final int maxChance = (int) (chance * 100);

			if (TextUtil.random(100) <= maxChance)
				farmingState[index] = 2;
		}
	}

	/* clearing the patch with a rake of a spade */

	public int getConfigValue(int flowerStage, int seedId, int plantState, int index) {
		if (farmingSeeds[index] >= 0x21 && farmingSeeds[index] <= 0x24 && farmingStages[index] > 3)
			return (GROWING << 6) + farmingSeeds[index];
		final FlowerData flowerData = FlowerData.forId(seedId);
		switch (flowerStage) {
		case 0:// weed
			return (GROWING << 6) + 0x00;
		case 1:// weed cleared
			return (GROWING << 6) + 0x01;
		case 2:
			return (GROWING << 6) + 0x02;
		case 3:
			return (GROWING << 6) + 0x03;
		}
		if (flowerData == null)
			return -1;
		if (flowerData.getEndingState() == flowerData.getStartingState() + flowerStage - 2)
			hasFullyGrown[index] = true;
		return (getPlantState(plantState) << 6) + flowerData.getStartingState() + flowerStage - 4;
	}

	/* planting the seeds */

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	/* harvesting the plant resulted */

	public int[] getFarmingStages() {
		return farmingStages;
	}

	/* putting compost onto the plant */

	public int[] getFarmingState() {
		return farmingState;
	}

	/* inspecting a plant */

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	/* opening the corresponding guide about the patch */

	public int getPlantState(int plantState) {
		switch (plantState) {
		case 0:
			return GROWING;
		case 1:
			return WATERED;
		case 2:
			return DISEASED;
		case 3:
			return DEAD;
		}
		return -1;
	}

	/* Curing the plant */

	public boolean guide(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null)
			return false;
		return true;
	}

	/* Planting scarecrow to push off the birds */

	public boolean harvest(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null)
			return false;
		final FlowerData flowerData = FlowerData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null)
			return false;
		if (!player.getInventory().contains(FarmingConstants.SPADE)) {
			player.getInterfaceSet().sendStatement(null, "You need a spade to harvest here.");
			return true;
		}

		player.playAnimation(new Animation(FarmingConstants.SPADE_ANIM));
		final Action<Player> action = new Action<Player>(2, false, player) {

			@Override
			public void execute() {
				resetFlowers(flowerFieldsData.getFlowerIndex());
				farmingStages[flowerFieldsData.getFlowerIndex()] = 3;
				farmingTimer[flowerFieldsData.getFlowerIndex()] = World.getWorld().getUptime();
				player.playAnimation(new Animation(FarmingConstants.SPADE_ANIM));
				player.sendMessage("You harvest the crop, and get some vegetables.");
				player.getInventory().add(
						new Item(flowerData.getHarvestId(), flowerData.getHarvestId() == 5099
								|| flowerData.getHarvestId() == 5100 ? 3 : 1));
				player.getSkillSet().addExperience(Skill.FARMING, flowerData.getHarvestXp());
				stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.stopAnimation();
				super.stop();
			}

		};

		player.startAction(action);
		return true;
	}

	public boolean inspect(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null)
			return false;
		final InspectData inspectData = InspectData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		final FlowerData flowerData = FlowerData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		if (farmingState[flowerFieldsData.getFlowerIndex()] == 2) {
			player.getInterfaceSet().sendStatement(null, "This plant is diseased. Use a plant cure on it to cure it,",
					"or clear the patch with a spade.");
			return true;
		}
		else if (farmingState[flowerFieldsData.getFlowerIndex()] == 3) {
			player.getInterfaceSet().sendStatement(null, "This plant is dead. You did not cure it while it was diseased.",
					"Clear the patch with a spade.");
			return true;
		}
		if (farmingStages[flowerFieldsData.getFlowerIndex()] == 0)
			player.getInterfaceSet().sendStatement(null, "This is an flower patch. The soil has not been treated.",
					"The patch needs weeding.");
		else if (farmingStages[flowerFieldsData.getFlowerIndex()] == 3)
			player.getInterfaceSet().sendStatement(null, "This is an flower patch. The soil has not been treated.",
					"The patch is empty and weeded.");
		else if (inspectData != null && flowerData != null) {
			player.sendMessage("You bend down and start to inspect the patch...");

			player.playAnimation(new Animation(1331));

			World.getWorld().schedule(new ScheduledTask(5, false) {

				@Override
				public void execute() {
					if (farmingStages[flowerFieldsData.getFlowerIndex()] - 4 < inspectData.getMessages().length - 2)
						player.getInterfaceSet().sendStatement(null,
								inspectData.getMessages()[farmingStages[flowerFieldsData.getFlowerIndex()] - 4]);
					else if (farmingStages[flowerFieldsData.getFlowerIndex()] < flowerData.getEndingState()
							- flowerData.getStartingState() + 4)
						player.getInterfaceSet().sendStatement(null,
								inspectData.getMessages()[inspectData.getMessages().length - 2]);
					else
						player.getInterfaceSet().sendStatement(null,
								inspectData.getMessages()[inspectData.getMessages().length - 1]);
					stop();
				}

				@Override
				public void stop() {
					player.playAnimation(new Animation(1332));
					super.stop();
				}
			});
		}
		return true;
	}

	/* reseting the patches */

	public boolean plantScareCrow(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null || itemId != SCARECROW)
			return false;
		if (farmingStages[flowerFieldsData.getFlowerIndex()] != 3) {
			player.sendMessage("You need to clear the patch before planting a scarecrow");
			return false;
		}
		player.getInventory().remove(new Item(SCARECROW));
		player.playAnimation(new Animation(832));
		World.getWorld().schedule(new ScheduledTask(2, false) {

			@Override
			public void execute() {
				player.sendMessage("You put a scarecrow on the flower patch, and some weeds start to grow around it.");
				farmingSeeds[flowerFieldsData.getFlowerIndex()] = 0x24;
				farmingStages[flowerFieldsData.getFlowerIndex()] = 4;
				farmingTimer[flowerFieldsData.getFlowerIndex()] = World.getWorld().getUptime();
				stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.stopAnimation();
				super.stop();
			}
		});
		return true;
	}

	/* checking if the patch is raked */

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		final FlowerData flowerData = FlowerData.forId(seedId);
		if (flowerFieldsData == null || flowerData == null)
			return false;
		if (farmingStages[flowerFieldsData.getFlowerIndex()] != 3) {
			player.sendMessage("You can't plant a seed here.");
			return false;
		}
		if (flowerData.getLevelRequired() > player.getSkillSet().getSkill(Skill.FARMING).getCurrentLevel()) {
			player.getInterfaceSet().sendStatement(null,
					"You need a farming level of " + flowerData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().contains(FarmingConstants.SEED_DIBBER)) {
			player.getInterfaceSet().sendStatement(null, "You need a seed dibber to plant seed here.");
			return true;
		}

		player.playAnimation(new Animation(FarmingConstants.SEED_DIBBING));
		farmingStages[flowerFieldsData.getFlowerIndex()] = 4;
		player.getInventory().add(new Item(seedId));

		World.getWorld().schedule(new ScheduledTask(3, false) {

			@Override
			public void execute() {
				farmingState[flowerFieldsData.getFlowerIndex()] = 0;
				farmingSeeds[flowerFieldsData.getFlowerIndex()] = seedId;
				farmingTimer[flowerFieldsData.getFlowerIndex()] = World.getWorld().getUptime();
				player.getSkillSet().addExperience(Skill.FARMING, flowerData.getPlantingXp());
				stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				super.stop();
			}

		});
		return true;
	}

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034)
			return false;
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null)
			return false;
		if (farmingStages[flowerFieldsData.getFlowerIndex()] != 3
				|| farmingState[flowerFieldsData.getFlowerIndex()] == 5) {
			player.sendMessage("This patch doesn't need compost.");
			return true;
		}
		player.getInventory().remove(new Item(itemId));
		player.getInventory().add(new Item(1925));

		player.sendMessage("You pour some " + (itemId == 6034 ? "super" : "") + "compost on the patch.");
		player.playAnimation(new Animation(FarmingConstants.PUTTING_COMPOST));
		player.getSkillSet().addExperience(Skill.FARMING,
				itemId == 6034 ? Compost.SUPER_COMPOST_EXP_USE : Compost.COMPOST_EXP_USE);

		World.getWorld().schedule(new ScheduledTask(7, false) {

			@Override
			public void execute() {
				diseaseChance[flowerFieldsData.getFlowerIndex()] *= itemId == 6032 ? COMPOST_CHANCE
						: SUPERCOMPOST_CHANCE;
				farmingState[flowerFieldsData.getFlowerIndex()] = 5;
				stop();
			}

			@Override
			public void stop() {
				player.stopAnimation();
				super.stop();
			}
		});
		return true;
	}

	@SuppressWarnings("unused")
	private void resetFlowers() {
		for (int i = 0; i < farmingStages.length; i++) {
			farmingSeeds[i] = 0;
			farmingState[i] = 0;
			diseaseChance[i] = 0;
		}
	}

	private void resetFlowers(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
	}

	public void setDiseaseChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
	}

	public void setFarmingSeeds(int i, int flowerSeeds) {
		this.farmingSeeds[i] = flowerSeeds;
	}

	public void setFarmingStages(int i, int flowerStages) {
		this.farmingStages[i] = flowerStages;
	}

	public void setFarmingState(int i, int flowerState) {
		this.farmingState[i] = flowerState;
	}

	public void setFarmingTimer(int i, long flowerTimer) {
		this.farmingTimer[i] = flowerTimer;
	}

	public void updateFlowerStates() {
		// ardougne - phasmatys - falador - catherby
		final int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++)
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.send(new ConfigEvent(FLOWER_PATCH_CONFIGS, configValue));

	}

	public boolean waterPatch(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null)
			return false;
		final FlowerData flowerData = FlowerData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null)
			return false;
		if (farmingState[flowerFieldsData.getFlowerIndex()] == 1
				|| farmingStages[flowerFieldsData.getFlowerIndex()] <= 1
				|| farmingStages[flowerFieldsData.getFlowerIndex()] == flowerData.getEndingState()
						- flowerData.getStartingState() + 4) {
			player.sendMessage("This patch doesn't need watering.");
			return true;
		}
		player.getInventory().remove(new Item(itemId));
		player.getInventory().add(new Item(itemId == 5333 ? itemId - 2 : itemId - 1));

		if (!player.getInventory().contains(FarmingConstants.RAKE)) {
			player.getInterfaceSet().sendStatement(null, "You need a seed dibber to plant seed here.");
			return true;
		}
		player.sendMessage("You water the patch.");
		player.playAnimation(new Animation(FarmingConstants.WATERING_CAN_ANIM));

		World.getWorld().schedule(new ScheduledTask(5, false) {

			@Override
			public void execute() {
				diseaseChance[flowerFieldsData.getFlowerIndex()] *= WATERING_CHANCE;
				farmingState[flowerFieldsData.getFlowerIndex()] = 1;
				stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.stopAnimation();
				super.stop();
			}
		});
		return true;
	}

}
