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
import org.apollo.game.model.skill.farming.FarmingConstants.Tools;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.TextUtil;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 24/02/12 Time: 20:34 To change
 * this template use File | Settings | File
 * Templates.
 */
@SuppressWarnings("javadoc")
public final class FruitTree {

	public enum FruitTreeData {
		APPLE(5496, 1955, 1, 27, new int[] { 5986, 9 }, 120, 0.20, 22, 8.5, 0x08, 0x14, 0x0e, 0x21, 0x22, 1199.5, 12,
				18), BANANA(5497, 1963, 1, 33, new int[] { 5386, 4 }, 120, 0.20, 28, 10.5, 0x23, 0x2f, 0x29, 0x3c,
				0x3d, 1750.5, 12, 18), ORANGE(5498, 2108, 1, 39, new int[] { 5406, 3 }, 120, 0.20, 35.5, 13.5, 0x48,
				0x54, 0x4e, 0x61, 0x62, 2470.2, 12, 18), CURRY(5499, 5970, 1, 42, new int[] { 5416, 5 }, 120, 0.25, 40,
				15, 0x63, 0x6f, 0x69, 0x7c, 0x7d, 2906.9, 12, 18), PINEAPPLE(5500, 2114, 1, 51, new int[] { 5982, 10 },
				120, 0.25, 57, 21.5, 0x88, 0x94, 0x8e, 0xa1, 0xa2, 4605.7, 12, 18), PAPAYA(5501, 5972, 1, 57,
				new int[] { 2114, 10 }, 120, 0.25, 72, 27, 0xa3, 0xaf, 0xa9, 0xbc, 0xbd, 6146.4, 12, 18), PALM(5502,
				5974, 1, 68, new int[] { 5972, 15 }, 120, 0.25, 170.5, 41.5, 0xc8, 0xd4, 0xce, 0xe1, 0xe2, 10150.1, 12,
				18);

		public static FruitTreeData forId(int saplingId) {
			return saplings.get(saplingId);
		}

		private int saplingId;

		private int harvestId;

		private int saplingAmount;

		private int levelRequired;

		private int[] paymentToWatch;

		private int growthTime;

		private double diseaseChance;

		private double plantingXp;

		private double harvestXp;

		private int startingState;

		private int endingState;

		private int limitState;

		private int stumpState;

		private int checkHealthState;

		private double checkHealthExperience;

		private int diseaseDiffValue;

		private int deathDiffValue;

		private static Map<Integer, FruitTreeData> saplings = new HashMap<Integer, FruitTreeData>();

		static {
			for (final FruitTreeData data : FruitTreeData.values())
				saplings.put(data.saplingId, data);
		}

		FruitTreeData(int saplingId, int harvestId, int saplingAmount, int levelRequired, int[] paymentToWatch,
				int growthTime, double diseaseChance, double plantingXp, double harvestXp, int startingState,
				int endingState, int limitState, int stumpState, int checkHealthState, double checkHealthExperience,
				int diseaseDiffValue, int deathDiffValue) {
			this.saplingId = saplingId;
			this.harvestId = harvestId;
			this.saplingAmount = saplingAmount;
			this.levelRequired = levelRequired;
			this.paymentToWatch = paymentToWatch;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
			this.limitState = limitState;
			this.stumpState = stumpState;
			this.checkHealthState = checkHealthState;
			this.checkHealthExperience = checkHealthExperience;
			this.diseaseDiffValue = diseaseDiffValue;
			this.deathDiffValue = deathDiffValue;
		}

		public int getCheckHealthState() {
			return checkHealthState;
		}

		public double getCheckHealthXp() {
			return checkHealthExperience;
		}

		public int getDeathDiffValue() {
			return deathDiffValue;
		}

		public double getDiseaseChance() {
			return diseaseChance;
		}

		public int getDiseaseDiffValue() {
			return diseaseDiffValue;
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

		public int getLimitState() {
			return limitState;
		}

		public int[] getPaymentToWatch() {
			return paymentToWatch;
		}

		public double getPlantingXp() {
			return plantingXp;
		}

		public int getSapplingAmount() {
			return saplingAmount;
		}

		public int getSapplingId() {
			return saplingId;
		}

		public int getStartingState() {
			return startingState;
		}

		public int getStumpState() {
			return stumpState;
		}
	}

	// set of global constants for Farming

	public enum FruitTreeFieldsData {
		BRIMHAVEN(0, new Position[] { new Position(2764, 3212), new Position(2765, 3213) }, 2330), CATHERBY(1,
				new Position[] { new Position(2860, 3433), new Position(2861, 3434) }, 2331), TREE_STRONGHOLD(2,
				new Position[] { new Position(2475, 3445), new Position(2476, 3446) }, 2343), TREE_VILLAGE(3,
				new Position[] { new Position(2489, 3179), new Position(2890, 3180) }, 2344);
		private int fruitTreeIndex;

		private Position[] fruitTreePosition;

		private int npcId;

		private static Map<Integer, FruitTreeFieldsData> npcsProtecting = new HashMap<Integer, FruitTreeFieldsData>();

		static {
			for (final FruitTreeFieldsData data : FruitTreeFieldsData.values())
				npcsProtecting.put(data.npcId, data);
		}

		public static FruitTreeFieldsData forId(int npcId) {
			return npcsProtecting.get(npcId);
		}

		public static FruitTreeFieldsData forIdPosition(Position position) {
			for (final FruitTreeFieldsData fruitTreeFieldsData : FruitTreeFieldsData.values())
				if (FarmingConstants.inRangeArea(fruitTreeFieldsData.getFruitTreePosition()[0],
						fruitTreeFieldsData.getFruitTreePosition()[1], position))
					return fruitTreeFieldsData;
			return null;
		}

		FruitTreeFieldsData(int fruitTreeIndex, Position[] fruitTreePosition, int npcId) {
			this.fruitTreeIndex = fruitTreeIndex;
			this.fruitTreePosition = fruitTreePosition;
			this.npcId = npcId;
		}

		public int getFruitTreeIndex() {
			return fruitTreeIndex;
		}

		public Position[] getFruitTreePosition() {
			return fruitTreePosition;
		}

		public int getNpcId() {
			return npcId;
		}
	}

	public enum InspectData {

		APPLE(5496, new String[][] { { "The apple sapling has only just been planted." },
				{ "The apple sapling grows into a small stump." }, { "The apple stump grows a little larger." },
				{ "The apple tree grows a small canopy." }, { "The apple tree grows a second small canopy." },
				{ "The apple tree grows larger." }, { "The apple tree is ready to be harvested." }, }), BANANA(5497,
				new String[][] { { "The banana sapling has only just been planted." },
						{ "The banana sapling grows 3 segments high, with 2 leaves." },
						{ "The banana tree grows 2 more leaves." },
						{ "The banana tree grows 5 segments high, and has some small bananas." },
						{ "The banana tree grows a bit larger." }, { "The banana tree grows a bit larger." },
						{ "The banana tree is ready to be harvested." }, }), ORANGE(5498, new String[][] {
				{ "The orange sapling has only just been planted." }, { "The orange sapling grows slightly taller." },
				{ "The orange sapling grows even taller." }, { "The orange tree grows a small canopy." },
				{ "The orange tree grows taller." }, { "The orange tree grows wider and taller." },
				{ "The oranges on the tree are ready to be harvested." } }), CURRY(5499, new String[][] {
				{ "The curry sapling has only just been planted." }, { "The curry trunk grows towards the north." },
				{ "The curry trunk grows towards the north." }, { "The curry tree grows upwards." },
				{ "The curry trunk grows towards the south." }, { "The curry trunk grows towards the south." },
				{ "The curry tree is ready to be harvested." } }), PINEAPPLE(5500, new String[][] {
				{ "The pineapple sapling has only just been planted." }, { "The pineapple plant grows larger." },
				{ "The pineapple plant base turns brown." }, { "The pineapple plant grows larger." },
				{ "The pineapple plant grows larger." }, { "The pineapple plant grows larger." },
				{ "The pineapple plant is ready to be harvested." } }), PAPAYA(5501, new String[][] {
				{ "The papaya sapling has only just been planted." }, { "The papaya sapling grows a little larger." },
				{ "The papaya tree grows a little larger." }, { "The papaya tree grows a bit larger." },
				{ "The papaya tree grows some small yellow fruit." }, { "The papaya tree grows larger." },
				{ "The papaya tree is ready to be harvested." } }), PALM(5502, new String[][] {
				{ "The palm sapling has only just been planted." }, { "The palm sapling grows a little larger." },
				{ "The palm stump grows a little larger." }, { "The palm tree grows a small canopy." },
				{ "The palm tree grows taller." }, { "The palm tree grows more leaves." },
				{ "The palm tree is ready to be harvested." } });
		private int saplingId;

		private String[][] messages;

		private static Map<Integer, InspectData> saplings = new HashMap<Integer, InspectData>();

		static {
			for (final InspectData data : InspectData.values())
				saplings.put(data.saplingId, data);
		}

		public static InspectData forId(int saplingId) {
			return saplings.get(saplingId);
		}

		InspectData(int saplingId, String[][] messages) {
			this.saplingId = saplingId;
			this.messages = messages;
		}

		public String[][] getMessages() {
			return messages;
		}

		public int getSapplingId() {
			return saplingId;
		}
	}

	private final Player player;

	private static final double COMPOST_CHANCE = 0.9;

	private static final double SUPERCOMPOST_CHANCE = 0.7;

	private static final double CLEARING_EXPERIENCE = 4;

	// Farming data
	public int[] farmingStages = new int[4];

	public int[] farmingSeeds = new int[4];

	public int[] farmingState = new int[4];

	public long[] farmingTimer = new long[4];

	public double[] diseaseChance = { 1, 1, 1, 1 };

	public boolean[] hasFullyGrown = { false, false, false, false };

	/* This is the enum holding the seeds info */

	public boolean[] farmingWatched = { false, false, false, false, false, false, false, false };

	/* This is the enum data about the different patches */

	public static final int MAIN_FRUIT_TREE_CONFIG = 503;

	/* This is the enum that hold the different data for inspecting the plant */

	public FruitTree(Player player) {
		this.player = player;
	}

	/* update all the patch states */

	public boolean checkIfRaked(int objectX, int objectY) {
		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData
				.forIdPosition(new Position(objectX, objectY));
		if (fruitTreeFieldsData == null)
			return false;
		if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] == 3)
			return true;
		return false;
	}

	/* getting the different config values */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData
				.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (fruitTreeFieldsData == null || itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)
			return false;
		if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] == 3)
			return true;
		if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] <= 3) {
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
				if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] <= 2) {
					farmingStages[fruitTreeFieldsData.getFruitTreeIndex()]++;
					player.getInventory().add(new Item(6055));
				}
				else {
					farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] = 3;
					stop();
				}
				player.getSkillSet().addExperience(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[fruitTreeFieldsData.getFruitTreeIndex()] = World.getWorld().getUptime();
				updateFruitTreeStates();
				if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] == 3) {
					stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetFruitTrees(fruitTreeFieldsData.getFruitTreeIndex());
				player.sendMessage("You clear the patch.");
				player.stopAnimation();
				super.stop();
			}
		});
		return true;

	}

	/* getting the plant states */

	/**
	 * Woodcutting action
	 * @param = tree id
	 * @param x = tree x location
	 * @param y = tree y location
	 * @return
	 */

	public boolean cut(final int x, final int y) {

		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData.forIdPosition(new Position(x, y));
		if (fruitTreeFieldsData == null)
			return false;
		final FruitTreeData fruitTreeData = FruitTreeData.forId(farmingSeeds[fruitTreeFieldsData.getFruitTreeIndex()]);
		if (fruitTreeData == null)
			return false;

		if (player.getInventory().freeSlots() <= 0) {
			player.sendMessage("Not enough space in your inventory.");
			return true;
		}

		final Tools tool = FarmingConstants.Tools.getTool(player);
		if (tool == null) {
			player.sendMessage("You do not have an axe.");
			return true;
		}
		if (tool.getLevel() <= player.getSkillSet().getSkill(Skill.WOODCUTTING).getCurrentLevel()) {
			player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
			return true;
		}
		player.sendMessage("You swing your axe at the tree.");
		player.playAnimation(tool.getAnimation());
		final Action<Player> action = new Action<Player>(5, false, player) {
			@Override
			public void execute() {
				player.sendMessage("You cut down the tree.");
				farmingState[fruitTreeFieldsData.getFruitTreeIndex()] = 6;
				updateFruitTreeStates();
				stop();

			}

			@Override
			public void stop() {
				player.stopAnimation();
				super.stop();
			}
		};

		player.startAction(action);
		return true;
	}

	/* calculating the disease chance and making the plant grow */

	@SuppressWarnings("unused")
	private void displayAll() {
		for (int i = 0; i < farmingStages.length; i++) {
			System.out.println("index : " + i);
			System.out.println("state : " + farmingState[i]);
			System.out.println("sapling : " + farmingSeeds[i]);
			System.out.println("level : " + farmingStages[i]);
			System.out.println("timer : " + farmingTimer[i]);
			System.out.println("disease chance : " + diseaseChance[i]);
			System.out.println("-----------------------------------------------------------------");
		}
	}

	public void doCalculations() {
		for (int i = 0; i < farmingSeeds.length; i++) {
			if (farmingStages[i] > 0 && farmingStages[i] <= 3 && World.getWorld().getUptime() - farmingTimer[i] >= 5) {
				farmingStages[i]--;
				farmingTimer[i] = World.getWorld().getUptime();
				updateFruitTreeStates();
				continue;
			}
			final FruitTreeData fruitTreeData = FruitTreeData.forId(farmingSeeds[i]);
			if (fruitTreeData == null)
				continue;

			final long difference = World.getWorld().getUptime() - farmingTimer[i];
			final long growth = fruitTreeData.getGrowthTime();
			final int nbStates = fruitTreeData.getEndingState() - fruitTreeData.getStartingState();
			final int state = (int) (difference * nbStates / growth);
			if (farmingTimer[i] == 0 || farmingState[i] == 2 || farmingState[i] == 3 || state > nbStates)
				continue;
			if (4 + state != farmingStages[i]) {
				if (farmingStages[i] + fruitTreeData.getStartingState() == fruitTreeData.getLimitState() + 3) {
					farmingStages[i] = fruitTreeData.getEndingState() - fruitTreeData.getStartingState() + 7;
					farmingState[i] = 3;
					updateFruitTreeStates();
					continue;
				}
				farmingStages[i] = 4 + state;
				if (farmingStages[i] <= 4 + state)
					for (int j = farmingStages[i]; j <= 4 + state; j++)
						doStateCalculation(i);
				updateFruitTreeStates();
			}
		}
	}

	public void doStateCalculation(int index) {
		if (farmingState[index] == 2)
			return;
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 1)
			if (farmingWatched[index]) {
				farmingState[index] = 0;
				final FruitTreeData bushesData = FruitTreeData.forId(farmingSeeds[index]);
				if (bushesData == null)
					return;
				System.out.println(farmingSeeds[index]);
				final int difference = bushesData.getEndingState() - bushesData.getStartingState();
				final int growth = bushesData.getGrowthTime();
				farmingTimer[index] += growth / difference;
				modifyStage(index);
			} else
				farmingState[index] = 2;

		if (farmingState[index] == 5 && farmingStages[index] != 2)
			farmingState[index] = 0;

		if (farmingState[index] == 0 && farmingStages[index] >= 5 && !hasFullyGrown[index]) {
			final FruitTreeData fruitTreeData = FruitTreeData.forId(farmingSeeds[index]);
			if (fruitTreeData == null)
				return;

			final double chance = diseaseChance[index] * fruitTreeData.getDiseaseChance();
			final int maxChance = (int) chance * 100;
			if (TextUtil.random(100) <= maxChance)
				farmingState[index] = 1;
		}
	}

	/* calculations about the diseasing chance */

	public int getConfigValue(int fruitTreeStage, int saplingId, int plantState, int index) {
		final FruitTreeData fruitTreeData = FruitTreeData.forId(saplingId);
		switch (fruitTreeStage) {
		case 0:// weed
			return 0x00;
		case 1:// weed cleared
			return 0x01;
		case 2:
			return 0x02;
		case 3:
			return 0x03;
		}
		if (fruitTreeData == null)
			return -1;
		if (fruitTreeStage > fruitTreeData.getEndingState() - fruitTreeData.getStartingState() - 1)
			hasFullyGrown[index] = true;

		if (plantState == 6)
			return fruitTreeData.getStumpState();

		if (getPlantState(plantState, fruitTreeData, fruitTreeStage) == 3)
			return fruitTreeData.getCheckHealthState();

		return getPlantState(plantState, fruitTreeData, fruitTreeStage);
	}

	/* clearing the patch with a rake of a spade */

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	/* planting the saplings */

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	public int[] getFarmingStages() {
		return farmingStages;
	}

	/* harvesting the plant resulted */

	public int[] getFarmingState() {
		return farmingState;
	}

	/* lowering the stage */

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	/* putting compost onto the plant */

	public boolean[] getFarmingWatched() {
		return farmingWatched;
	}

	/* inspecting a plant */

	public int getPlantState(int plantState, FruitTreeData fruitTreeData, int fruitTreeStage) {
		final int value = fruitTreeData.getStartingState() + fruitTreeStage - 4;
		switch (plantState) {
		case 0:
			return value;
		case 1:
			return value + fruitTreeData.getDiseaseDiffValue();
		case 2:
			return value + fruitTreeData.getDeathDiffValue();
		case 3:
			return fruitTreeData.getCheckHealthState();
		}
		return -1;
	}

	/* opening the corresponding guide about the patch */

	public boolean guide(int objectX, int objectY) {
		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData
				.forIdPosition(new Position(objectX, objectY));
		if (fruitTreeFieldsData == null)
			return false;
		return true;
	}

	/* Curing the plant */

	public boolean harvestOrCheckHealth(int objectX, int objectY) {
		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData
				.forIdPosition(new Position(objectX, objectY));
		if (fruitTreeFieldsData == null)
			return false;
		final FruitTreeData fruitTreeData = FruitTreeData.forId(farmingSeeds[fruitTreeFieldsData.getFruitTreeIndex()]);
		if (fruitTreeData == null)
			return false;
		System.out.println(farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] + fruitTreeData.getStartingState()
				+ "  " + (fruitTreeData.getLimitState() - 4));
		if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] + fruitTreeData.getStartingState() == fruitTreeData
				.getLimitState() + 4) {
			cut(objectX, objectY);
			return true;
		}
		if (player.getInventory().freeSlots() <= 0) {
			player.sendMessage("Not enough space in your inventory.");
			return true;
		}
		player.playAnimation(new Animation(832));
		final Action<Player> action = new Action<Player>(2, false, player) {

			@Override
			public void execute() {
				if (player.getInventory().freeSlots() <= 0) {
					stop();
					return;
				}

				if (farmingState[fruitTreeFieldsData.getFruitTreeIndex()] == 3) {
					player.sendMessage("You examine the tree for signs of disease and find that it's in perfect health.");
					player.getSkillSet().addExperience(Skill.FARMING, fruitTreeData.getCheckHealthXp());
					farmingState[fruitTreeFieldsData.getFruitTreeIndex()] = 0;
					hasFullyGrown[fruitTreeFieldsData.getFruitTreeIndex()] = false;
					farmingTimer[fruitTreeFieldsData.getFruitTreeIndex()] = World.getWorld().getUptime()
							- fruitTreeData.getGrowthTime();
					modifyStage(fruitTreeFieldsData.getFruitTreeIndex());
					stop();
					return;
				}
				player.sendMessage("You harvest the crop, and pick a fruit.");
				player.getInventory().add(new Item(fruitTreeData.getHarvestId()));
				player.getSkillSet().addExperience(Skill.FARMING, fruitTreeData.getHarvestXp());
				farmingTimer[fruitTreeFieldsData.getFruitTreeIndex()] = World.getWorld().getUptime();
				final int difference = fruitTreeData.getEndingState() - fruitTreeData.getStartingState();
				final int growth = fruitTreeData.getGrowthTime();
				lowerStage(fruitTreeFieldsData.getFruitTreeIndex(), growth - growth / difference
						* (difference + 5 - farmingStages[fruitTreeFieldsData.getFruitTreeIndex()]));
				modifyStage(fruitTreeFieldsData.getFruitTreeIndex());
				stop();
			}

			@Override
			public void stop() {
				super.stop();
			}
		};

		player.startAction(action);
		return true;
	}

	public boolean inspect(int objectX, int objectY) {
		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData
				.forIdPosition(new Position(objectX, objectY));
		if (fruitTreeFieldsData == null)
			return false;
		final InspectData inspectData = InspectData.forId(farmingSeeds[fruitTreeFieldsData.getFruitTreeIndex()]);
		final FruitTreeData fruitTreeData = FruitTreeData.forId(farmingSeeds[fruitTreeFieldsData.getFruitTreeIndex()]);
		if (farmingState[fruitTreeFieldsData.getFruitTreeIndex()] == 1) {
			player.getInterfaceSet().sendStatement(null, "This plant is diseased. Use a plant cure on it to cure it,",
					"or clear the patch with a spade.");
			return true;
		}
		else if (farmingState[fruitTreeFieldsData.getFruitTreeIndex()] == 2) {
			player.getInterfaceSet().sendStatement(null, "This plant is dead. You did not cure it while it was diseased.",
					"Clear the patch with a spade.");
			return true;
		}
		else if (farmingState[fruitTreeFieldsData.getFruitTreeIndex()] == 3) {
			player.getInterfaceSet().sendStatement(null, "This plant has fully grown. You can check it's health",
					"to gain some farming experiences.");
			return true;
		}
		else if (farmingState[fruitTreeFieldsData.getFruitTreeIndex()] == 6) {
			player.getInterfaceSet().sendStatement(null, "This is a fruit tree stump, to remove it, use a",
					"spade on it to clear the patch");
			return true;
		}
		if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] == 0)
			player.getInterfaceSet().sendStatement(null, "This is a fruit tree patch. The soil has not been treated.",
					"The patch needs weeding.");
		else if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] == 3)
			player.getInterfaceSet().sendStatement(null, "This is a fruit tree patch. The soil has not been treated.",
					"The patch is empty and weeded.");
		else if (inspectData != null && fruitTreeData != null) {
			player.sendMessage("You bend down and start to inspect the patch...");

			player.playAnimation(new Animation(1331));

			World.getWorld().schedule(new ScheduledTask(5, false) {

				@Override
				public void execute() {
					if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] - 4 < inspectData.getMessages().length - 2)
						player.getInterfaceSet().sendStatement(null,
								inspectData.getMessages()[farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] - 4]);
					else if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] < fruitTreeData.getEndingState()
							- fruitTreeData.getStartingState() + 2)
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

	/* checking if the patch is raked */

	public void lowerStage(int index, int timer) {
		hasFullyGrown[index] = false;
		farmingTimer[index] -= timer;
	}

	public void modifyStage(int i) {
		final FruitTreeData fruitTreeData = FruitTreeData.forId(farmingSeeds[i]);
		if (fruitTreeData == null)
			return;
		final long difference = World.getWorld().getUptime() - farmingTimer[i];
		final long growth = fruitTreeData.getGrowthTime();
		final int nbStates = fruitTreeData.getEndingState() - fruitTreeData.getStartingState();
		final int state = (int) (difference * nbStates / growth);
		farmingStages[i] = 4 + state;
		updateFruitTreeStates();

	}

	public boolean plantSapling(int objectX, int objectY, final int saplingId) {
		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData
				.forIdPosition(new Position(objectX, objectY));
		final FruitTreeData fruitTreeData = FruitTreeData.forId(saplingId);
		if (fruitTreeFieldsData == null || fruitTreeData == null)
			return false;
		if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] != 3) {
			player.sendMessage("You can't plant a sapling here.");
			return true;
		}
		if (fruitTreeData.getLevelRequired() > player.getSkillSet().getSkill(Skill.FARMING).getCurrentLevel()) {
			player.getInterfaceSet().sendStatement(null,
					"You need a farming level of " + fruitTreeData.getLevelRequired() + " to plant this sapling.");
			return true;
		}

		if (!player.getInventory().contains(FarmingConstants.TROWEL)) {
			player.getInterfaceSet().sendStatement(null, "You need a trowel to plant the sapling here.");
			return true;
		}
		player.playAnimation(new Animation(FarmingConstants.PLANTING_POT_ANIM));
		farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] = 4;
		player.getInventory().remove(new Item(saplingId));

		World.getWorld().schedule(new ScheduledTask(3, false) {

			@Override
			public void execute() {
				farmingState[fruitTreeFieldsData.getFruitTreeIndex()] = 0;
				farmingSeeds[fruitTreeFieldsData.getFruitTreeIndex()] = saplingId;
				farmingTimer[fruitTreeFieldsData.getFruitTreeIndex()] = World.getWorld().getUptime();
				player.getSkillSet().addExperience(Skill.FARMING, fruitTreeData.getPlantingXp());
				stop();
			}

			@Override
			public void stop() {
				updateFruitTreeStates();
				super.stop();
			}
		});
		return true;
	}

	public boolean pruneArea(int objectX, int objectY, int itemId) {
		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData
				.forIdPosition(new Position(objectX, objectY));
		if (fruitTreeFieldsData == null
				|| itemId != FarmingConstants.SECATEURS && itemId != FarmingConstants.MAGIC_SECATEURS)
			return false;
		final FruitTreeData fruitTreeData = FruitTreeData.forId(farmingSeeds[fruitTreeFieldsData.getFruitTreeIndex()]);
		if (fruitTreeData == null)
			return false;
		if (farmingState[fruitTreeFieldsData.getFruitTreeIndex()] != 1) {
			player.sendMessage("This area doesn't need to be pruned.");
			return true;
		}
		player.playAnimation(new Animation(FarmingConstants.PRUNING_ANIM));

		farmingState[fruitTreeFieldsData.getFruitTreeIndex()] = 0;
		World.getWorld().schedule(new ScheduledTask(15, false) {

			@Override
			public void execute() {
				player.sendMessage("You prune the area with your secateurs.");
				stop();
			}

			@Override
			public void stop() {
				updateFruitTreeStates();
				player.stopAnimation();
				super.stop();
			}
		});

		return true;

	}

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034)
			return false;
		final FruitTreeFieldsData fruitTreeFieldsData = FruitTreeFieldsData
				.forIdPosition(new Position(objectX, objectY));
		if (fruitTreeFieldsData == null)
			return false;
		if (farmingStages[fruitTreeFieldsData.getFruitTreeIndex()] != 3
				|| farmingState[fruitTreeFieldsData.getFruitTreeIndex()] == 5) {
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
				diseaseChance[fruitTreeFieldsData.getFruitTreeIndex()] *= itemId == 6032 ? COMPOST_CHANCE
						: SUPERCOMPOST_CHANCE;
				farmingState[fruitTreeFieldsData.getFruitTreeIndex()] = 5;
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

	private void resetFruitTrees(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		hasFullyGrown[index] = false;
		farmingWatched[index] = false;
	}

	public void setDiseaseChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
	}

	public void setFarmingSeeds(int i, int allotmentSeeds) {
		this.farmingSeeds[i] = allotmentSeeds;
	}

	public void setFarmingStages(int i, int allotmentStages) {
		this.farmingStages[i] = allotmentStages;
	}

	public void setFarmingState(int i, int allotmentState) {
		this.farmingState[i] = allotmentState;
	}

	public void setFarmingTimer(int i, long allotmentTimer) {
		this.farmingTimer[i] = allotmentTimer;
	}

	public void setFarmingWatched(int i, boolean allotmentWatched) {
		this.farmingWatched[i] = allotmentWatched;
	}

	public void updateFruitTreeStates() {
		// brimhaven - catherby - tree stronghold - tree village
		final int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++)
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);
		// System.out.println("config 1 " + configValues[1]);
		// System.out.println("config 0 " + configValues[0]);
		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.send(new ConfigEvent(MAIN_FRUIT_TREE_CONFIG, configValue));
	}

}
