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
public final class Hops {

	public enum HopsData {
		BARLEY(5305, 6006, 4, 3, new int[] { 6032, 3 }, 40, 0.35, 8.5, 9.5, 0x31, 0x35), HAMMERSTONE(5307, 5994, 4, 4,
				new int[] { 6010, 1 }, 40, 0.35, 9, 10, 0x04, 0x08), ASGARNIAN(5308, 5996, 4, 8, new int[] { 5458, 1 },
				40, 0.30, 10.5, 12, 0x0b, 0x10), JUTE(5306, 5931, 3, 13, new int[] { 6008, 6 }, 40, 0.30, 13, 14.5,
				0x38, 0x3d), YANILLIAN(5309, 5998, 4, 16, new int[] { 5968, 1 }, 40, 0.25, 14.5, 16, 0x13, 0x19), KRANDORIAN(
				5310, 6000, 4, 21, new int[] { 5478 }, 40, 0.25, 17.5, 19.5, 0x1c, 0x23), WILDBLOOD(5311, 6002, 4, 28,
				new int[] { 6012, 1 }, 40, 0.20, 23, 26, 0x26, 0x2e), ;

		public static HopsData forId(int seedId) {
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

		private static Map<Integer, HopsData> seeds = new HashMap<Integer, HopsData>();

		static {
			for (final HopsData data : HopsData.values())
				seeds.put(data.seedId, data);
		}

		HopsData(int seedId, int harvestId, int seedAmount, int levelRequired, int[] paymentToWatch, int growthTime,
				double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState) {
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

	public enum HopsFieldsData {

		LUMBRIDGE(0, new Position[] { new Position(3227, 3313, 0), new Position(3231, 3317, 0) }, 2333), MCGRUBOR(1,
				new Position[] { new Position(2664, 3523, 0), new Position(2669, 3528, 0) }, 2334), YANILLE(2,
				new Position[] { new Position(2574, 3103, 0), new Position(2577, 3106, 0) }, 2332), ENTRANA(3,
				new Position[] { new Position(2809, 3335, 0), new Position(2812, 3338, 0) }, 2327);

		private int hopsIndex;

		private Position[] hopsPosition;

		private int npcId;

		private static Map<Integer, HopsFieldsData> npcsProtecting = new HashMap<Integer, HopsFieldsData>();

		static {
			for (final HopsFieldsData data : HopsFieldsData.values())
				npcsProtecting.put(data.npcId, data);
		}

		public static HopsFieldsData forId(int npcId) {
			return npcsProtecting.get(npcId);
		}

		public static HopsFieldsData forIdPosition(Position position) {
			for (final HopsFieldsData hopsFieldsData : HopsFieldsData.values())
				if (FarmingConstants.inRangeArea(hopsFieldsData.getHopsPosition()[0],
						hopsFieldsData.getHopsPosition()[1], position))
					return hopsFieldsData;
			return null;
		}

		HopsFieldsData(int hopsIndex, Position[] hopsPosition, int npcId) {
			this.hopsIndex = hopsIndex;
			this.hopsPosition = hopsPosition;
			this.npcId = npcId;
		}

		public int getHopsIndex() {
			return hopsIndex;
		}

		public Position[] getHopsPosition() {
			return hopsPosition;
		}

		public int getNpcId() {
			return npcId;
		}
	}

	public enum InspectData {

		BARLEY(5305, new String[][] { { "The barley seeds have only just been planted." },
				{ "Grain heads develop at the upper part of the stalks,", "as the barley grows taller." },
				{ "The barley grows taller, the heads weighing", "slightly on the stalks." },
				{ "The barley grows taller." },
				{ "The barley is ready to harvest. The heads of grain", "are weighing down heavily on the stalks!" } }), HAMMERSTONE(
				5307, new String[][] { { "The Hammerstone seeds have only just been planted." },
						{ "The Hammerstone hops plant grows a little bit taller." },
						{ "The Hammerstone hops plant grows a bit taller." },
						{ "The Hammerstone hops plant grows a bit taller." },
						{ "The Hammerstone hops plant is ready to harvest." } }), ASGARNIAN(5308, new String[][] {
				{ "The Asgarnian seeds have only just been planted." },
				{ "The Asgarnian hops plant grows a bit taller." }, { "The Asgarnian hops plant grows a bit taller." },
				{ "The Asgarnian hops plant grows a bit taller." },
				{ "The upper new leaves appear dark green to the", "rest of the plant." },
				{ "The Asgarnian hops plant is ready to harvest." } }), JUTE(5306, new String[][] {
				{ "The Jute seeds have only just been planted." }, { "The jute plants grow taller." },
				{ "The jute plants grow taller." }, { "The jute plants grow taller." },
				{ "The jute plant grows taller. They are as high", "as the player." },
				{ "The jute plants are ready to harvest." } }), YANILLIAN(5309, new String[][] {
				{ "The Yanillian seeds have only just been planted." },
				{ "The Yanillian hops plant grows a bit taller." }, { "The Yanillian hops plant grows a bit taller." },
				{ "The Yanillian hops plant grows a bit taller." },
				{ "The new leaves on the top of the Yanillian hops", "plant are dark green." },
				{ "The new leaves on the top of the Yanillian hops", "plant are dark green." },
				{ "The Yanillian hops plant is ready to harvest." } }), KRANDORIAN(5310, new String[][] {
				{ "The Krandorian seeds have only just been planted." },
				{ "The Krandorian plant grows a bit taller." }, { "The Krandorian plant grows a bit taller." },
				{ "The Krandorian plant grows a bit taller." },
				{ "The new leaves on top of the Krandorian plant are", "dark green." },
				{ "The Krandorian plant grows a bit taller." },
				{ "The new leaves on top of the Krandorian plant", "are dark green." },
				{ "The Krandorian plant is ready for harvesting." } }), WILDBLOOD(5311, new String[][] {
				{ "The wildblood seeds have only just been planted." },
				{ "The wildblood hops plant grows a bit taller." }, { "The wildblood hops plant grows a bit taller." },
				{ "The wildblood hops plant grows a bit taller." }, { "The wildblood hops plant grows a bit taller." },
				{ "The wildblood hops plant grows a bit taller." }, { "The wildblood hops plant grows a bit taller." },
				{ "The new leaves at the top of the wildblood hops plant", "are dark green." },
				{ "The wildblood hops plant is ready to harvest." } });
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

	private static final int START_HARVEST_AMOUNT = 3;

	private static final int END_HARVEST_AMOUNT = 41;

	private static final double WATERING_CHANCE = 0.5;

	private static final double COMPOST_CHANCE = 0.9;

	private static final double SUPERCOMPOST_CHANCE = 0.7;

	private static final double CLEARING_EXPERIENCE = 4;

	// Farming data
	public int[] farmingStages = new int[4];

	public int[] farmingSeeds = new int[4];

	public int[] farmingHarvest = new int[4];

	public int[] farmingState = new int[4];

	public long[] farmingTimer = new long[4];

	public double[] diseaseChance = { 1, 1, 1, 1 };

	/* set of the constants for the patch */

	public boolean[] hasFullyGrown = { false, false, false, false };

	public boolean[] farmingWatched = { false, false, false, false };

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;

	public static final int WATERED = 0x01;

	public static final int DISEASED = 0x02;

	/* This is the enum holding the seeds info */

	public static final int DEAD = 0x03;

	/* This is the enum data about the different patches */

	public static final int MAIN_HOPS_CONFIG = 506;

	/* This is the enum that hold the different data for inspecting the plant */

	public Hops(Player player) {
		this.player = player;
	}

	/* update all the patch states */

	public boolean checkIfRaked(int objectX, int objectY) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null)
			return false;
		if (farmingStages[hopsFieldsData.getHopsIndex()] == 3)
			return true;
		return false;
	}

	/* getting the different config values */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (hopsFieldsData == null || itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)
			return false;
		if (farmingStages[hopsFieldsData.getHopsIndex()] == 3)
			return true;
		if (farmingStages[hopsFieldsData.getHopsIndex()] <= 3) {
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
				if (farmingStages[hopsFieldsData.getHopsIndex()] <= 2) {
					farmingStages[hopsFieldsData.getHopsIndex()]++;
					player.getInventory().add(new Item(6055));
				}
				else {
					farmingStages[hopsFieldsData.getHopsIndex()] = 3;
					stop();
				}
				player.getSkillSet().addExperience(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[hopsFieldsData.getHopsIndex()] = World.getWorld().getUptime();
				updateHopsStates();
				if (farmingStages[hopsFieldsData.getHopsIndex()] == 3) {
					stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetHops(hopsFieldsData.getHopsIndex());
				player.sendMessage("You clear the patch.");
				player.stopAnimation();
				super.stop();
			}
		});
		return true;

	}

	/* getting the plant states */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null || itemId != 6036)
			return false;
		final HopsData hopsData = HopsData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		if (hopsData == null)
			return false;
		if (farmingState[hopsFieldsData.getHopsIndex()] != 2) {
			player.sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().remove(new Item(itemId));
		player.getInventory().add(new Item(229));
		player.playAnimation(new Animation(FarmingConstants.CURING_ANIM));

		farmingState[hopsFieldsData.getHopsIndex()] = 0;
		World.getWorld().schedule(new ScheduledTask(7, false) {

			@Override
			public void execute() {
				player.sendMessage("You cure the plant with a plant cure.");
				stop();
			}

			@Override
			public void stop() {
				updateHopsStates();
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
			System.out.println("harvest : " + farmingHarvest[i]);
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
				updateHopsStates();
				continue;
			}
			final HopsData hopsData = HopsData.forId(farmingSeeds[i]);
			if (hopsData == null)
				continue;

			final long difference = World.getWorld().getUptime() - farmingTimer[i];
			final long growth = hopsData.getGrowthTime();
			final int nbStates = hopsData.getEndingState() - hopsData.getStartingState();
			final int state = (int) (difference * nbStates / growth);
			if (farmingTimer[i] == 0 || farmingState[i] == 3 || state > nbStates)
				continue;
			if (4 + state != farmingStages[i]) {
				farmingStages[i] = 4 + state;
				if (farmingStages[i] <= 4 + state)
					for (int j = farmingStages[i]; j <= 4 + state; j++)
						doStateCalculation(i);
				updateHopsStates();
			}
		}
	}

	/* calculations about the diseasing chance */

	public void doStateCalculation(int index) {
		if (farmingState[index] == 3)
			return;
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 2)
			if (farmingWatched[index]) {
				farmingState[index] = 0;
				final HopsData hopsData = HopsData.forId(farmingSeeds[index]);
				if (hopsData == null)
					return;
				final int difference = hopsData.getEndingState() - hopsData.getStartingState();
				final int growth = hopsData.getGrowthTime();
				farmingTimer[index] += growth / difference;
				modifyStage(index);
			} else
				farmingState[index] = 3;

		if (farmingState[index] == 1) {
			diseaseChance[index] *= 2;
			farmingState[index] = 0;
		}

		if (farmingState[index] == 5 && farmingStages[index] != 3)
			farmingState[index] = 0;

		if (farmingState[index] == 0 && farmingStages[index] >= 5 && !hasFullyGrown[index]) {
			final HopsData hopsData = HopsData.forId(farmingSeeds[index]);
			if (hopsData == null)
				return;

			final double chance = diseaseChance[index] * hopsData.getDiseaseChance();
			final int maxChance = (int) chance * 100;
			if (TextUtil.random(100) <= maxChance)
				farmingState[index] = 2;
		}
	}

	/* watering the patch */

	public int getConfigValue(int hopsStage, int seedId, int plantState, int index) {
		final HopsData hopsData = HopsData.forId(seedId);
		switch (hopsStage) {
		case 0:// weed
			return (GROWING << 6) + 0x00;
		case 1:// weed cleared
			return (GROWING << 6) + 0x01;
		case 2:
			return (GROWING << 6) + 0x02;
		case 3:
			return (GROWING << 6) + 0x03;
		}
		if (hopsData == null)
			return -1;
		if (hopsData.getEndingState() == hopsData.getStartingState() + hopsStage - 1)
			hasFullyGrown[index] = true;

		return (getPlantState(plantState) << 6) + hopsData.getStartingState() + hopsStage - 4;
	}

	/* clearing the patch with a rake of a spade */

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	/* planting the seeds */

	public int[] getFarmingHarvest() {
		return farmingHarvest;
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

	public boolean[] getFarmingWatched() {
		return farmingWatched;
	}

	/* Curing the plant */

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

	public boolean guide(int objectX, int objectY) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null)
			return false;
		return true;
	}

	/* checking if the patch is raked */

	public boolean harvest(int objectX, int objectY) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null)
			return false;
		final HopsData hopsData = HopsData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		if (hopsData == null)
			return false;
		if (!player.getInventory().contains(FarmingConstants.SPADE)) {
			player.getInterfaceSet().sendStatement(null, "You need a spade to harvest here.");
			return true;
		}
		player.playAnimation(new Animation(FarmingConstants.SPADE_ANIM));
		final Action<Player> action = new Action<Player>(2, false, player) {
			@Override
			public void execute() {
				if (farmingHarvest[hopsFieldsData.getHopsIndex()] == 0)
					farmingHarvest[hopsFieldsData.getHopsIndex()] = (int) (1 + (START_HARVEST_AMOUNT + TextUtil
							.random(END_HARVEST_AMOUNT - START_HARVEST_AMOUNT))
							* (player.getEquipment().contains(7409) ? 1.10 : 1));
				if (farmingHarvest[hopsFieldsData.getHopsIndex()] == 1) {
					resetHops(hopsFieldsData.getHopsIndex());
					farmingStages[hopsFieldsData.getHopsIndex()] = 3;
					farmingTimer[hopsFieldsData.getHopsIndex()] = World.getWorld().getUptime();
					stop();
					return;
				}
				if (player.getInventory().freeSlots() <= 0) {
					stop();
					return;
				}
				farmingHarvest[hopsFieldsData.getHopsIndex()]--;
				player.playAnimation(new Animation(FarmingConstants.SPADE_ANIM));
				player.sendMessage("You harvest the crop, and get some vegetables.");
				player.getInventory().add(new Item(hopsData.getHarvestId()));
				player.getSkillSet().addExperience(Skill.FARMING, hopsData.getHarvestXp());
			}

			@Override
			public void stop() {
				updateHopsStates();
				player.stopAnimation();
				super.stop();
			}
		};

		player.startAction(action);
		return true;
	}

	public boolean inspect(int objectX, int objectY) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null)
			return false;
		final InspectData inspectData = InspectData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		final HopsData hopsData = HopsData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		if (farmingState[hopsFieldsData.getHopsIndex()] == 2) {
			player.getInterfaceSet().sendStatement(null, "This plant is diseased. Use a plant cure on it to cure it,",
					"or clear the patch with a spade.");
			return true;
		}
		else if (farmingState[hopsFieldsData.getHopsIndex()] == 3) {
			player.getInterfaceSet().sendStatement(null, "This plant is dead. You did not cure it while it was diseased.",
					"Clear the patch with a spade.");
			return true;
		}
		if (farmingStages[hopsFieldsData.getHopsIndex()] == 0)
			player.getInterfaceSet().sendStatement(null, "This is a hops patch. The soil has not been treated.",
					"The patch needs weeding.");
		else if (farmingStages[hopsFieldsData.getHopsIndex()] == 3)
			player.getInterfaceSet().sendStatement(null, "This is a hops patch. The soil has not been treated.",
					"The patch is empty and weeded.");
		else if (inspectData != null && hopsData != null) {
			player.sendMessage("You bend down and start to inspect the patch...");

			player.playAnimation(new Animation(1331));

			World.getWorld().schedule(new ScheduledTask(5, false) {

				@Override
				public void execute() {
					if (farmingStages[hopsFieldsData.getHopsIndex()] - 4 < inspectData.getMessages().length - 2)
						player.getInterfaceSet().sendStatement(null,
								inspectData.getMessages()[farmingStages[hopsFieldsData.getHopsIndex()] - 4]);
					else if (farmingStages[hopsFieldsData.getHopsIndex()] < hopsData.getEndingState()
							- hopsData.getStartingState() + 2)
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

	public void modifyStage(int i) {
		final HopsData hopsData = HopsData.forId(farmingSeeds[i]);
		if (hopsData == null)
			return;
		final long difference = World.getWorld().getUptime() - farmingTimer[i];
		final long growth = hopsData.getGrowthTime();
		final int nbStates = hopsData.getEndingState() - hopsData.getStartingState();
		final int state = (int) (difference * nbStates / growth);
		farmingStages[i] = 4 + state;
		updateHopsStates();

	}

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		final HopsData hopsData = HopsData.forId(seedId);
		if (hopsFieldsData == null || hopsData == null)
			return false;
		if (farmingStages[hopsFieldsData.getHopsIndex()] != 3) {
			player.sendMessage("You can't plant a seed here.");
			return false;
		}
		if (hopsData.getLevelRequired() > player.getSkillSet().getSkill(Skill.FARMING).getCurrentLevel()) {
			player.getInterfaceSet().sendStatement(null,
					"You need a farming level of " + hopsData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().contains(FarmingConstants.SEED_DIBBER)) {
			player.getInterfaceSet().sendStatement(null, "You need a seed dibber to plant seed here.");
			return true;
		}
		if (player.getInventory().getItemCount(hopsData.getSeedId()) < hopsData.getSeedAmount()) {
			player.getInterfaceSet().sendStatement(null,
					"You need atleast " + hopsData.getSeedAmount() + " seeds to plant here.");
			return true;
		}
		player.playAnimation(new Animation(FarmingConstants.SEED_DIBBING));
		farmingStages[hopsFieldsData.getHopsIndex()] = 4;
		player.getInventory().remove(new Item(seedId, hopsData.getSeedAmount()));

		World.getWorld().schedule(new ScheduledTask(3, false) {

			@Override
			public void execute() {
				farmingState[hopsFieldsData.getHopsIndex()] = 0;
				farmingSeeds[hopsFieldsData.getHopsIndex()] = seedId;
				farmingTimer[hopsFieldsData.getHopsIndex()] = World.getWorld().getUptime();
				player.getSkillSet().addExperience(Skill.FARMING, hopsData.getPlantingXp());
				stop();
			}

			@Override
			public void stop() {
				updateHopsStates();
				super.stop();
			}
		});
		return true;
	}

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034)
			return false;
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null)
			return false;
		if (farmingStages[hopsFieldsData.getHopsIndex()] != 3 || farmingState[hopsFieldsData.getHopsIndex()] == 5) {
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
				diseaseChance[hopsFieldsData.getHopsIndex()] *= itemId == 6032 ? COMPOST_CHANCE : SUPERCOMPOST_CHANCE;
				farmingState[hopsFieldsData.getHopsIndex()] = 5;
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

	private void resetHops(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		farmingHarvest[index] = 0;
		hasFullyGrown[index] = false;
		farmingWatched[index] = false;
	}

	public void setDiseaseChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
	}

	public void setFarmingHarvest(int i, int allotmentHarvest) {
		this.farmingHarvest[i] = allotmentHarvest;
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

	public void updateHopsStates() {
		// lumbridge - mc grubor - yanille - entrana
		final int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++)
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.send(new ConfigEvent(MAIN_HOPS_CONFIG, configValue));

	}

	public boolean waterPatch(int objectX, int objectY, int itemId) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null)
			return false;
		final HopsData hopsData = HopsData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		if (hopsData == null)
			return false;
		if (farmingState[hopsFieldsData.getHopsIndex()] == 1
				|| farmingStages[hopsFieldsData.getHopsIndex()] <= 1
				|| farmingStages[hopsFieldsData.getHopsIndex()] == hopsData.getEndingState()
						- hopsData.getStartingState() + 4) {
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
				diseaseChance[hopsFieldsData.getHopsIndex()] *= WATERING_CHANCE;
				farmingState[hopsFieldsData.getHopsIndex()] = 1;
				stop();
			}

			@Override
			public void stop() {
				updateHopsStates();
				player.stopAnimation();
				super.stop();
			}
		});
		return true;
	}

}
