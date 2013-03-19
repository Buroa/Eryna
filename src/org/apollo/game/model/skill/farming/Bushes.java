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
public final class Bushes {

	public enum BushesData {
		REDBERRY(5101, 1951, 1, 10, new int[] { 5478, 4 }, 100, 0.20, 11.5, 4.5, 0x05, 0x0e, 0x09, 0x3a, 64), CADAVABERRY(
				5102, 753, 1, 22, new int[] { 5968, 3 }, 140, 0.20, 18, 7, 0x0f, 0x19, 0x14, 0x3b, 102.5), DWELLBERRY(
				5103, 2126, 1, 36, new int[] { 5406, 3 }, 140, 0.20, 31.5, 12, 0x1a, 0x25, 0x20, 0x3c, 177.5), JANGERBERRY(
				5104, 247, 1, 48, new int[] { 5982, 6 }, 160, 0.20, 50.5, 19, 0x26, 0x32, 0x2d, 0x3d, 284.5), WHITEBERRY(
				5105, 239, 1, 59, new int[] { 6004, 8 }, 160, 0.20, 78, 29, 0x33, 0x3f, 0x3a, 0x3e, 437.5), POISONIVYBERRY(
				5106, 6018, 1, 70, null, 160, 0.20, 120, 45, 0xc5, 0xd1, 0xcc, 0x3f, 674);

		public static BushesData forId(int seedId) {
			return seeds.get(seedId);
		}

		private int seedId;

		private int harvestId;

		private int seedAmount;

		private int levelRequired;

		private int[] paymentToWatch;

		private int growthTime;

		private double diseaseChance;

		private double plantingXp;

		private double harvestXp;

		private int startingState;

		private int endingState;

		private int limitState;

		private int checkHealthState;

		private double checkHealthExperience;

		private static Map<Integer, BushesData> seeds = new HashMap<Integer, BushesData>();

		static {
			for (final BushesData data : BushesData.values())
				seeds.put(data.seedId, data);
		}

		BushesData(int seedId, int harvestId, int seedAmount, int levelRequired, int[] paymentToWatch, int growthTime,
				double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState,
				int limitState, int checkHealthState, double checkHealthExperience) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.seedAmount = seedAmount;
			this.levelRequired = levelRequired;
			this.paymentToWatch = paymentToWatch;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
			this.limitState = limitState;
			this.checkHealthState = checkHealthState;
			this.checkHealthExperience = checkHealthExperience;
		}

		public int getCheckHealthState() {
			return checkHealthState;
		}

		public double getCheckHealthXp() {
			return checkHealthExperience;
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

		public int getLimitState() {
			return limitState;
		}

		public int[] getPaymentToWatch() {
			return paymentToWatch;
		}

		public double getPlantingXp() {
			return plantingXp;
		}

		public int getSeedAmount() {
			return seedAmount;
		}

		public int getSeedId() {
			return seedId;
		}

		public int getStartingState() {
			return startingState;
		}
	}

	// set of global constants for Farming

	public enum BushesFieldsData {
		ETCETERIA(0, new Position[] { new Position(2591, 3863), new Position(2592, 3864) }, 2337), SOUTH_ARDOUGNE(1,
				new Position[] { new Position(2617, 3225), new Position(2618, 3226) }, 2338), CHAMPION_GUILD(2,
				new Position[] { new Position(3181, 3357), new Position(3182, 3358) }, 2335), RIMMINGTON(3,
				new Position[] { new Position(2940, 3221), new Position(2941, 3222) }, 2336);

		private int bushesIndex;

		private Position[] bushesPosition;

		private int npcId;

		private static Map<Integer, BushesFieldsData> npcsProtecting = new HashMap<Integer, BushesFieldsData>();

		static {
			for (final BushesFieldsData data : BushesFieldsData.values())
				npcsProtecting.put(data.npcId, data);
		}

		public static BushesFieldsData forId(int npcId) {
			return npcsProtecting.get(npcId);
		}

		public static BushesFieldsData forIdPosition(Position position) {
			for (final BushesFieldsData bushesFieldsData : BushesFieldsData.values())
				if (FarmingConstants.inRangeArea(bushesFieldsData.getBushesPosition()[0],
						bushesFieldsData.getBushesPosition()[1], position))
					return bushesFieldsData;
			return null;
		}

		BushesFieldsData(int bushesIndex, Position[] bushesPosition, int npcId) {
			this.bushesIndex = bushesIndex;
			this.bushesPosition = bushesPosition;
			this.npcId = npcId;
		}

		public int getBushesIndex() {
			return bushesIndex;
		}

		public Position[] getBushesPosition() {
			return bushesPosition;
		}

		public int getNpcId() {
			return npcId;
		}
	}

	public enum InspectData {

		REDBERRY(5101, new String[][] { { "The Redberry seeds have only just been planted." },
				{ "The Redberry bush grows larger." }, { "The Redberry bush grows larger." },
				{ "The Redberry bush grows small, unripe,", "green berries." },
				{ "The berries grow larger, and pink." },
				{ "The Redberry bush is ready to harvest.", "The berries on the bush are red." }, }), CADAVABERRY(5102,
				new String[][] { { "The Cadavaberry seeds have only just been planted." },
						{ "The Cadavaberry bush grows larger." }, { "The Cadavaberry bush grows larger." },
						{ "The Cadavaberry bush grows larger." },
						{ "The Cadavaberry bush grows small, unripe,", "green berries." },
						{ "The berries grow larger, and pink." },
						{ "The Cadavaberry bush is ready to harvest.", "The berries on the bush are purple." } }), DWELLBERRY(
				5103, new String[][] { { "The Dwellbery seeds have only just been planted." },
						{ "The Dwellbery bush grows larger." }, { "The Dwellbery bush grows larger." },
						{ "The Dwellbery bush grows larger." }, { "The Dwellbery bush grows larger." },
						{ "The Dwellbery bush grows small, unripe,", "green berries." },
						{ "The berries grow larger, and light blue." },
						{ "The Dwellbery bush is ready to harvest.", "The berries on the bush are blue." }, }), JANGERBERRY(
				5104, new String[][] { { "The Jangerberry seeds have only just been planted." },
						{ "The Jangerberry bush grows larger." }, { "The Jangerberry bush grows larger." },
						{ "The Jangerberry bush grows larger." }, { "The Jangerberry bush grows larger." },
						{ "The Jangerberry bush grows small, unripe,", "green berries." },
						{ "The berries grow larger." }, { "The berries grow larger, and light green." },
						{ "The Jangerberry bush is ready to harvest.", "The berries on the bush are green." } }), WHITEBERRY(
				5105, new String[][] { { "The Whiteberry seeds have only just been planted." },
						{ "The Whiteberry bush grows larger." }, { "The Whiteberry bush grows larger." },
						{ "The Whiteberry bush grows larger." }, { "The Whiteberry bush grows larger." },
						{ "The Whiteberry bush grows larger." },
						{ "The Whiteberry bush grows small, unripe,", "green berries." },
						{ "The berries grow larger." },
						{ "The Whiteberry bush is ready to harvest.", "The berries on the bush are white." }, }), POISONIVYBERRY(
				5106, new String[][] { { "The Poison ivy seeds have only just been planted." },
						{ "The Poison ivy bush grows larger." }, { "The Poison ivy bush grows larger." },
						{ "The Poison ivy bush grows larger." }, { "The Poison ivy bush grows larger." },
						{ "The Poison ivy bush grows small, unripe,", "green berries." },
						{ "The berries grow larger." }, { "The berries grow larger, and light green." },
						{ "The Poison ivy bush is ready to harvest.", "The berries on the bush are pale yellow." } });
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

	private static final double COMPOST_CHANCE = 0.9;

	private static final double SUPERCOMPOST_CHANCE = 0.7;

	private static final double CLEARING_EXPERIENCE = 4;

	// Farming data
	public int[] farmingStages = new int[4];

	public int[] farmingSeeds = new int[4];

	public int[] farmingState = new int[4];

	public long[] farmingTimer = new long[4];

	public double[] diseaseChance = { 1, 1, 1, 1 };

	/* set of the constants for the patch */

	public boolean[] hasFullyGrown = { false, false, false, false };

	public boolean[] bushesWatched = { false, false, false, false };

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;

	public static final int DISEASED = 0x01;

	public static final int DEAD = 0x02;

	/* This is the enum holding the seeds info */

	public static final int CHECK = 0x03;

	/* This is the enum data about the different patches */

	public static final int MAIN_BUSHES_CONFIG = 509;

	/* This is the enum that hold the different data for inspecting the plant */

	public Bushes(Player player) {
		this.player = player;
	}

	/* update all the patch states */

	public boolean checkIfRaked(int objectX, int objectY) {
		final BushesFieldsData bushesFieldData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldData == null)
			return false;
		if (farmingStages[bushesFieldData.getBushesIndex()] == 3)
			return true;
		return false;
	}

	/* getting the different config values */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (bushesFieldsData == null || itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)
			return false;
		if (farmingStages[bushesFieldsData.getBushesIndex()] == 3)
			return true;
		if (farmingStages[bushesFieldsData.getBushesIndex()] <= 3) {
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
				if (farmingStages[bushesFieldsData.getBushesIndex()] <= 2) {
					farmingStages[bushesFieldsData.getBushesIndex()]++;
					player.getInventory().add(new Item(6055));
				}
				else {
					farmingStages[bushesFieldsData.getBushesIndex()] = 3;
					stop();
				}
				player.getSkillSet().addExperience(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[bushesFieldsData.getBushesIndex()] = World.getWorld().getUptime();
				updateBushesStates();
				if (farmingStages[bushesFieldsData.getBushesIndex()] == 3) {
					stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetBushes(bushesFieldsData.getBushesIndex());
				player.sendMessage("You clear the patch.");
				player.stopAnimation();
				super.stop();
			}

		});
		return true;

	}

	/* getting the plant states */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null || itemId != 6036)
			return false;
		final BushesData bushesData = BushesData.forId(farmingSeeds[bushesFieldsData.getBushesIndex()]);
		if (bushesData == null)
			return false;
		if (farmingState[bushesFieldsData.getBushesIndex()] != 1) {
			player.sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().remove(new Item(itemId));
		player.getInventory().add(new Item(229));
		player.playAnimation(new Animation(FarmingConstants.CURING_ANIM));
		farmingState[bushesFieldsData.getBushesIndex()] = 0;

		World.getWorld().schedule(new ScheduledTask(7, false) {

			@Override
			public void execute() {
				player.sendMessage("You cure the plant with a plant cure.");
				stop();
			}

			@Override
			public void stop() {
				updateBushesStates();
				player.stopAnimation();
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

	public void doCalculations() {
		for (int i = 0; i < farmingSeeds.length; i++) {
			if (farmingStages[i] > 0 && farmingStages[i] <= 3 && World.getWorld().getUptime() - farmingTimer[i] >= 5) {
				farmingStages[i]--;
				farmingTimer[i] = World.getWorld().getUptime();
				updateBushesStates();
				continue;
			}
			final BushesData bushesData = BushesData.forId(farmingSeeds[i]);
			if (bushesData == null)
				continue;

			final long difference = World.getWorld().getUptime() - farmingTimer[i];
			final long growth = bushesData.getGrowthTime();
			final int nbStates = bushesData.getEndingState() - bushesData.getStartingState();
			final int state = (int) (difference * nbStates / growth);
			if (farmingTimer[i] == 0 || farmingState[i] == 2 || farmingState[i] == 3 || state > nbStates)
				continue;
			if (4 + state != farmingStages[i]) {
				if (farmingStages[i] == bushesData.getEndingState() - bushesData.getStartingState() - 1) {
					farmingStages[i] = bushesData.getEndingState() - bushesData.getStartingState() + 4;
					farmingState[i] = 3;
					updateBushesStates();
					return;
				}
				farmingStages[i] = 4 + state;
				if (farmingStages[i] <= 4 + state)
					for (int j = farmingStages[i]; j <= 4 + state; j++)
						doStateCalculation(i);
				updateBushesStates();
			}
		}
	}

	/* calculations about the diseasing chance */

	public void doStateCalculation(int index) {
		if (farmingState[index] == 2)
			return;
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 1)
			if (bushesWatched[index]) {
				farmingState[index] = 0;
				final BushesData bushesData = BushesData.forId(farmingSeeds[index]);
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
			final BushesData bushesData = BushesData.forId(farmingSeeds[index]);
			if (bushesData == null)
				return;

			final double chance = diseaseChance[index] * bushesData.getDiseaseChance();
			final int maxChance = (int) chance * 100;
			if (TextUtil.random(100) <= maxChance)
				farmingState[index] = 1;
		}
	}

	/* clearing the patch with a rake of a spade */

	public int getConfigValue(int bushesStage, int seedId, int plantState, int index) {
		final BushesData bushesData = BushesData.forId(seedId);
		switch (bushesStage) {
		case 0:// weed
			return (GROWING << 6) + 0x00;
		case 1:// weed cleared
			return (GROWING << 6) + 0x01;
		case 2:
			return (GROWING << 6) + 0x02;
		case 3:
			return (GROWING << 6) + 0x03;
		}
		if (bushesData == null)
			return -1;
		if (bushesStage > bushesData.getEndingState() - bushesData.getStartingState() - 1)
			hasFullyGrown[index] = true;
		if (getPlantState(plantState) == 3)
			return (getPlantState(plantState) << 6) + bushesData.getCheckHealthState();
		if (seedId == 5106)
			if (getPlantState(plantState) == 1)
				return bushesData.getStartingState() + bushesStage - 4 + 12;
			else if (getPlantState(plantState) == 2)
				return bushesData.getStartingState() + bushesStage - 4 + 20;
		return (getPlantState(plantState) << 6) + bushesData.getStartingState() + bushesStage - 4
				+ (getPlantState(plantState) == 2 ? -1 : 0);
	}

	/* planting the seeds */

	public double[] getFarmingChance() {
		return diseaseChance;
	}

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	/* harvesting the plant resulted */

	public int[] getFarmingStages() {
		return farmingStages;
	}

	/* lowering the stage */

	public int[] getFarmingState() {
		return farmingState;
	}

	/* putting compost onto the plant */

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	/* inspecting a plant */

	public boolean[] getFarmingWatched() {
		return bushesWatched;
	}

	/* opening the corresponding guide about the patch */

	public int getPlantState(int plantState) {
		switch (plantState) {
		case 0:
			return GROWING;
		case 1:
			return DISEASED;
		case 2:
			return DEAD;
		case 3:
			return CHECK;
		}
		return -1;
	}

	/* Curing the plant */

	public boolean guide(int objectX, int objectY) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null)
			return false;
		return true;
	}

	public boolean harvestOrCheckHealth(int objectX, int objectY) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null)
			return false;
		final BushesData bushesData = BushesData.forId(farmingSeeds[bushesFieldsData.getBushesIndex()]);
		if (bushesData == null)
			return false;
		if (player.getInventory().freeSlots() <= 0) {
			player.sendMessage("Not enough space in your inventory.");
			return true;
		}
		player.playAnimation(new Animation(832));
		final Action<Player> action = new Action<Player>(2, false, player) {

			@Override
			public void execute() {
				if (!(player.getInventory().freeSlots() <= 0)) {
					stop();
					return;
				}

				if (farmingState[bushesFieldsData.getBushesIndex()] == 3) {
					player.sendMessage("You examine the bush for signs of disease and find that it's in perfect health.");
					player.getSkillSet().addExperience(Skill.FARMING, bushesData.getCheckHealthXp());
					farmingState[bushesFieldsData.getBushesIndex()] = 0;
					hasFullyGrown[bushesFieldsData.getBushesIndex()] = false;
					farmingTimer[bushesFieldsData.getBushesIndex()] = World.getWorld().getUptime()
							- bushesData.getGrowthTime();
					// bushesStages[bushesFieldsData.getBushesIndex()] -= 2;
					modifyStage(bushesFieldsData.getBushesIndex());
					stop();
					return;
				}
				player.sendMessage("You harvest the crop, and pick some berries.");
				player.getInventory().add(new Item(bushesData.getHarvestId()));
				player.getSkillSet().addExperience(Skill.FARMING, bushesData.getHarvestXp());
				farmingTimer[bushesFieldsData.getBushesIndex()] = World.getWorld().getUptime();
				final int difference = bushesData.getEndingState() - bushesData.getStartingState();
				final int growth = bushesData.getGrowthTime();
				lowerStage(bushesFieldsData.getBushesIndex(), growth - growth / difference
						* (difference + 5 - farmingStages[bushesFieldsData.getBushesIndex()]));
				modifyStage(bushesFieldsData.getBushesIndex());
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

	/* checking if the patch is raked */

	public boolean inspect(int objectX, int objectY) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null)
			return false;
		final InspectData inspectData = InspectData.forId(farmingSeeds[bushesFieldsData.getBushesIndex()]);
		final BushesData bushesData = BushesData.forId(farmingSeeds[bushesFieldsData.getBushesIndex()]);
		if (farmingState[bushesFieldsData.getBushesIndex()] == 1) {
			player.getInterfaceSet().sendStatement(null, "This plant is diseased. Use a plant cure on it to cure it,",
					"or clear the patch with a spade.");
			return true;
		}
		else if (farmingState[bushesFieldsData.getBushesIndex()] == 2) {
			player.getInterfaceSet().sendStatement(null, "This plant is dead. You did not cure it while it was diseased.",
					"Clear the patch with a spade.");
			return true;
		}
		else if (farmingState[bushesFieldsData.getBushesIndex()] == 3) {
			player.getInterfaceSet().sendStatement(null, "This plant has fully grown. You can check it's health",
					"to gain some farming experiences.");
			return true;
		}
		if (farmingStages[bushesFieldsData.getBushesIndex()] == 0)
			player.getInterfaceSet().sendStatement(null, "This is a bush patch. The soil has not been treated.",
					"The patch needs weeding.");
		else if (farmingStages[bushesFieldsData.getBushesIndex()] == 3)
			player.getInterfaceSet().sendStatement(null, "This is a bush patch. The soil has not been treated.",
					"The patch is empty and weeded.");
		else if (inspectData != null && bushesData != null) {
			player.sendMessage("You bend down and start to inspect the patch...");

			player.playAnimation(new Animation(1331));
			World.getWorld().schedule(new ScheduledTask(5, false) {

				@Override
				public void execute() {
					if (farmingStages[bushesFieldsData.getBushesIndex()] - 4 < inspectData.getMessages().length - 2)
						player.getInterfaceSet().sendStatement(null,
								inspectData.getMessages()[farmingStages[bushesFieldsData.getBushesIndex()] - 4]);
					else if (farmingStages[bushesFieldsData.getBushesIndex()] < bushesData.getEndingState()
							- bushesData.getStartingState() + 2)
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

	public void lowerStage(int index, int timer) {
		hasFullyGrown[index] = false;
		farmingTimer[index] -= timer;
	}

	public void modifyStage(int i) {
		final BushesData bushesData = BushesData.forId(farmingSeeds[i]);
		if (bushesData == null)
			return;
		final long difference = World.getWorld().getUptime() - farmingTimer[i];
		final long growth = bushesData.getGrowthTime();
		final int nbStates = bushesData.getEndingState() - bushesData.getStartingState();
		final int state = (int) (difference * nbStates / growth);
		farmingStages[i] = 4 + state;
		updateBushesStates();

	}

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		final BushesData bushesData = BushesData.forId(seedId);
		if (bushesFieldsData == null || bushesData == null)
			return false;
		if (farmingStages[bushesFieldsData.getBushesIndex()] != 3) {
			player.sendMessage("You can't plant a seed here.");
			return false;
		}
		if (bushesData.getLevelRequired() > player.getSkillSet().getSkill(Skill.FARMING).getCurrentLevel()) {
			player.getInterfaceSet().sendStatement(null,
					"You need a farming level of " + bushesData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().contains(FarmingConstants.SEED_DIBBER)) {
			player.getInterfaceSet().sendStatement(null, "You need a seed dibber to plant seed here.");
			return true;
		}
		if (player.getInventory().getItemCount(bushesData.getSeedId()) < bushesData.getSeedAmount()) {
			player.getInterfaceSet().sendStatement(null,
					"You need atleast " + bushesData.getSeedAmount() + " seeds to plant here.");
			return true;
		}
		player.playAnimation(new Animation(FarmingConstants.SEED_DIBBING));
		farmingStages[bushesFieldsData.getBushesIndex()] = 4;
		player.getInventory().remove(new Item(seedId, bushesData.getSeedAmount()));
		World.getWorld().schedule(new ScheduledTask(3, false) {

			@Override
			public void execute() {
				farmingState[bushesFieldsData.getBushesIndex()] = 0;
				farmingSeeds[bushesFieldsData.getBushesIndex()] = seedId;
				farmingTimer[bushesFieldsData.getBushesIndex()] = World.getWorld().getUptime();
				player.getSkillSet().addExperience(Skill.FARMING, bushesData.getPlantingXp());
			}

			@Override
			public void stop() {
				updateBushesStates();
				super.stop();
			}

		});
		return true;
	}

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034)
			return false;
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null)
			return false;

		if (farmingStages[bushesFieldsData.getBushesIndex()] != 3
				|| farmingState[bushesFieldsData.getBushesIndex()] == 5) {
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
				diseaseChance[bushesFieldsData.getBushesIndex()] *= itemId == 6032 ? COMPOST_CHANCE
						: SUPERCOMPOST_CHANCE;
				farmingState[bushesFieldsData.getBushesIndex()] = 5;
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

	private void resetBushes(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		hasFullyGrown[index] = false;
		bushesWatched[index] = false;
	}

	public void setFarmingChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
	}

	public void setFarmingSeeds(int i, int bushesSeeds) {
		this.farmingSeeds[i] = bushesSeeds;
	}

	public void setFarmingStages(int i, int bushesStages) {
		this.farmingStages[i] = bushesStages;
	}

	public void setFarmingState(int i, int bushesState) {
		this.farmingState[i] = bushesState;
	}

	public void setFarmingTimer(int i, long bushesTimer) {
		this.farmingTimer[i] = bushesTimer;
	}

	public void setFarmingWatched(int i, boolean bushesWatched) {
		this.bushesWatched[i] = bushesWatched;
	}

	public void updateBushesStates() {
		// etceteria - south ardougne - champion guild - rimmington
		final int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++)
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.send(new ConfigEvent(MAIN_BUSHES_CONFIG, configValue));
	}

}
