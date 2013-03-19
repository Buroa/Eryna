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

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/02/12 Time: 15:43 To change
 * this template use File | Settings | File
 * Templates.
 */
@SuppressWarnings("javadoc")
public final class Compost {

	public enum CompostBinLocations {
		NORTH_ARDOUGNE(0, new Position(2661, 3375, 0), FIRST_TYPE_COMPOST_BIN, 3), PHASMATYS(1, new Position(3610,
				3522, 0), SECOND_TYPE_COMPOST_BIN, 1), FALADOR(2, new Position(3056, 3312, 0), FIRST_TYPE_COMPOST_BIN,
				4), CATHERBY(3, new Position(2804, 3464, 0), FIRST_TYPE_COMPOST_BIN, 3);

		public static CompostBinLocations forId(int index) {
			return bins.get(index);
		}

		public static CompostBinLocations forPosition(Position position) {
			for (final CompostBinLocations compostBinLocations : CompostBinLocations.values())
				if (compostBinLocations.binPosition.equals(position))
					return compostBinLocations;
			return null;
		}

		private int compostIndex;

		private Position binPosition;

		private int binObjectId;

		private int objectFace;

		private static Map<Integer, CompostBinLocations> bins = new HashMap<Integer, CompostBinLocations>();

		static {
			for (final CompostBinLocations data : CompostBinLocations.values())
				bins.put(data.compostIndex, data);
		}

		CompostBinLocations(int compostIndex, Position binPosition, int binObjectId, int objectFace) {
			this.compostIndex = compostIndex;
			this.binPosition = binPosition;
			this.binObjectId = binObjectId;
			this.objectFace = objectFace;
		}

		public int getBinObjectId() {
			return binObjectId;
		}

		public Position getBinPosition() {
			return binPosition;
		}

		public int getCompostIndex() {
			return compostIndex;
		}

		public int getObjectFace() {
			return objectFace;
		}
	}

	public enum CompostBinStages {
		FIRST_TYPE(7808, 7813, 7809, 7810, 7811, 7812, 7814, 7815, 7816, 7817, 7828, 7829, 7830, 7831),
		SECOND_TYPE(7818, 7823, 7819, 7820, 7821, 7822, 7824, 7825, 7826, 7827, 7832, 7833, 7834, 7835);
		public static CompostBinStages forId(int binId) {
			return bins.get(binId);
		}

		private int binEmpty;

		private int closedBin;

		private int binWithCompostable;

		private int binFullOfCompostable;

		private int binWithSuperCompostable;

		private int binFullOFSuperCompostable;

		private int binWithCompost;

		private int binFullOfCompost;

		private int binWithSuperCompost;

		private int binFullOfSuperCompost;

		private int binWithTomatoes;

		private int binFullOfTomatoes;

		private int binWithRottenTomatoes;

		private int binFullOfRottenTomatoes;

		private static Map<Integer, CompostBinStages> bins = new HashMap<Integer, CompostBinStages>();

		static {
			for (final CompostBinStages data : CompostBinStages.values())
				bins.put(data.binEmpty, data);
		}

		CompostBinStages(int binEmpty, int closedBin, int binWithCompostable, int binFullOfCompostable,
				int binWithSuperCompostable, int binFullOFSuperCompostable, int binWithCompost, int binFullOfCompost,
				int binWithSuperCompost, int binFullOfSuperCompost, int binWithTomatoes, int binFullOfTomatoes,
				int binWithRottenTomatoes, int binFullOfRottenTomatoes) {
			this.binEmpty = binEmpty;
			this.closedBin = closedBin;
			this.binWithCompostable = binWithCompostable;
			this.binFullOfCompostable = binFullOfCompostable;
			this.binWithSuperCompostable = binWithSuperCompostable;
			this.binFullOFSuperCompostable = binFullOFSuperCompostable;
			this.binWithCompost = binWithCompost;
			this.binFullOfCompost = binFullOfCompost;
			this.binWithSuperCompost = binWithSuperCompost;
			this.binFullOfSuperCompost = binFullOfSuperCompost;
			this.binWithTomatoes = binWithTomatoes;
			this.binFullOfTomatoes = binFullOfTomatoes;
			this.binWithRottenTomatoes = binWithRottenTomatoes;
			this.binFullOfRottenTomatoes = binFullOfRottenTomatoes;
		}

		public int getBinEmpty() {
			return binEmpty;
		}

		public int getBinFullOfCompost() {
			return binFullOfCompost;
		}

		public int getBinFullOfCompostable() {
			return binFullOfCompostable;
		}

		public int getBinFullOfRottenTomatoes() {
			return binFullOfRottenTomatoes;
		}

		public int getBinFullOfSuperCompost() {
			return binFullOfSuperCompost;
		}

		public int getBinFullOFSuperCompostable() {
			return binFullOFSuperCompostable;
		}

		public int getBinFullOfTomatoes() {
			return binFullOfTomatoes;
		}

		public int getBinWithCompost() {
			return binWithCompost;
		}

		public int getBinWithCompostable() {
			return binWithCompostable;
		}

		public int getBinWithRottenTomatoes() {
			return binWithRottenTomatoes;
		}

		public int getBinWithSuperCompost() {
			return binWithSuperCompost;
		}

		public int getBinWithSuperCompostable() {
			return binWithSuperCompostable;
		}

		public int getBinWithTomatoes() {
			return binWithTomatoes;
		}

		public int getClosedBin() {
			return closedBin;
		}
	}

	private final Player player;

	public int[] compostBins = new int[4];

	public long[] compostBinsTimer = new long[4];

	public int[] organicItemAdded = new int[4];

	/* setting up the experiences constants */

	public int tempCompostState;

	public static final double COMPOST_EXP_RETRIEVE = 4.5;

	public static final double SUPER_COMPOST_EXP_RETRIEVE = 8.5;

	public static final double COMPOST_EXP_USE = 18;

	public static final double SUPER_COMPOST_EXP_USE = 26;

	/* these are the constants related to compost making */

	public static final double ROTTEN_TOMATOES_EXP_RETRIEVE = 8.5;

	public static final int COMPOST = 6032;

	public static final int SUPER_COMPOST = 6034;

	public static final int ROTTE_TOMATO = 2518;

	public static final int TOMATO = 1982;

	public static final int FIRST_TYPE_COMPOST_BIN = 7808;

	public static final int SECOND_TYPE_COMPOST_BIN = 7818;

	// states - 2 bits plant - 6 bits
	public static final int COMPOST_EMPTY = 0x00;

	public static final int COMPOST_CONTAINS = 0x01;

	public static final int COMPOST_FULL_CLOSABLE = 0x0f;

	public static final int COMPOST_ROTTED_OPEN = 0x10;

	public static final int COMPOST_ROTTED_FULL_OPEN = 30;

	public static final int COMPOST_ROTTING = 31;

	public static final int SUPER_COMPOST_CONTAINS = 33;

	public static final int SUPER_COMPOST_FULL_CLOSABLE = 47;

	public static final int COMPOST_CONFIG = 511;

	public static final int[] COMPOST_ORGANIC = { 6055, 1942, 1957, 1965, 5986, 5504, 5982, 249, 251, 253, 255, 257,
			2998, 259, 261, 263, 3000, 265, 2481, 267, 269, 1951, 753, 2126, 247, 239, 6018 };

	/* this is the enum that stores the different locations of the compost bins */

	public static final int[] SUPER_COMPOST_ORGANIC = { 2114, 5978, 5980, 5982, 6004, 247, 6469 };

	/* this is the enum that stores the different compost bins stages */

	public Compost(Player player) {
		this.player = player;
	}

	/* update all the compost states */

	/* handle what happens when the player close the compost bin */
	public void closeCompostBin(final int index) {
		final int config = getConfigValue(index);
		if (config == SUPER_COMPOST_FULL_CLOSABLE)
			compostBins[index] = 200;
		else if (config == COMPOST_FULL_CLOSABLE)
			compostBins[index] = 100;
		else
			compostBins[index] = 300; // TOMATOES

		compostBinsTimer[index] = World.getWorld().getUptime();

		player.playAnimation(new Animation(835, 0));
		World.getWorld().schedule(new ScheduledTask(2, false) {

			@Override
			public void execute() {
				player.sendMessage("You close the compost bin, and its content start to rot.");
				updateCompostBin(index);
				stop();
			}

			@Override
			public void stop() {
				super.stop();
			}

		});
	}

	/* handle compost bin filling */
	@SuppressWarnings("unused")
	public void fillCompostBin(final Position binPosition, final int organicItemUsed) {
		final CompostBinLocations compostBinLocations = CompostBinLocations.forPosition(binPosition);
		final int index = compostBinLocations.getCompostIndex();
		if (compostBinLocations == null)
			return;
		int incrementFactor = 0;
		// setting up the different increments.
		for (final int normalCompost : COMPOST_ORGANIC)
			if (organicItemUsed == normalCompost)
				incrementFactor = 2;

		for (final int superCompost : SUPER_COMPOST_ORGANIC)
			if (organicItemUsed == superCompost)
				incrementFactor = 17;

		if (organicItemUsed == TOMATO)
			if (compostBins[index] % 77 == 0)
				incrementFactor = 77;
			else
				incrementFactor = 2;

		// checking if the item used was an organic item.
		if (incrementFactor == 0) {
			player.sendMessage("You need to put organic items into the compost bin in order to make compost.");
			return;
		}
		final int factor = incrementFactor;
		// launching the main event for filling the compost bin.
		World.getWorld().schedule(new ScheduledTask(2, false) {

			@Override
			public void execute() {
				if (!player.getInventory().contains(organicItemUsed) || organicItemAdded[index] == 15) {
					stop();
					return;
				}
				organicItemAdded[index]++;
				player.playAnimation(new Animation(832, 0));
				player.getInventory().remove(new Item(organicItemUsed));
				compostBins[index] += factor;
				updateCompostBin(index);
			}

			@Override
			public void stop() {
				player.stopAnimation();
				super.stop();
			}

		});
	}

	public int[] getCompostBins() {
		return compostBins;
	}

	public long[] getCompostBinsTimer() {
		return compostBinsTimer;
	}

	/* getting the different config values */
	public int getConfigValue(int index) {
		final int compostStage = compostBins[index];

		switch (compostStage) {
		case 255:
			return SUPER_COMPOST_FULL_CLOSABLE;
		case 1155:
		case 15:
			return COMPOST_FULL_CLOSABLE;
		case 300:
		case 200:
		case 100:
			return COMPOST_ROTTING;
		case 350:
		case 250:
		case 150:
			return COMPOST_ROTTED_FULL_OPEN;
		case 0:
			return COMPOST_EMPTY;
		}

		if (compostStage % 17 == 0) {
			if (organicItemAdded[index] == 15)
				return SUPER_COMPOST_FULL_CLOSABLE;
			else
				return SUPER_COMPOST_CONTAINS;
		} else if (organicItemAdded[index] == 15)
			return COMPOST_FULL_CLOSABLE;
		else
			return COMPOST_CONTAINS;
	}

	public int[] getOrganicItemAdded() {
		return organicItemAdded;
	}

	public boolean handleItemOnObject(int itemUsed, int objectId, int objectX, int objectY) {
		if (!(objectId >= 7836 && objectId <= 7839))
			return false;

		final int index = CompostBinLocations.forPosition(new Position(objectX, objectY)).getCompostIndex();
		final int config = getConfigValue(index);

		switch (config) {
		case COMPOST_ROTTED_FULL_OPEN:
		case COMPOST_ROTTED_OPEN:
			if (itemUsed == 1925)
				retrieveCompost(CompostBinLocations.forPosition(new Position(objectX, objectY)).getCompostIndex());
			else
				player.sendMessage("You might need some buckets to gather the compost.");
			return true;

		case COMPOST_EMPTY:
		case COMPOST_CONTAINS:
		case SUPER_COMPOST_CONTAINS:
			fillCompostBin(new Position(objectX, objectY), itemUsed);
			return true;

		}
		return false;
	}

	/* handling the item on object method */

	public boolean handleObjectClick(int objectId, int objectX, int objectY) {
		if (!(objectId >= 7836 && objectId <= 7839))
			return false;

		final int index = CompostBinLocations.forPosition(new Position(objectX, objectY)).getCompostIndex();
		final int config = getConfigValue(index);

		switch (config) {
		case COMPOST_FULL_CLOSABLE:
		case SUPER_COMPOST_FULL_CLOSABLE:
			closeCompostBin(CompostBinLocations.forPosition(new Position(objectX, objectY)).getCompostIndex());
			return true;

		case COMPOST_ROTTING:
			openCompostBin(CompostBinLocations.forPosition(new Position(objectX, objectY)).getCompostIndex());
			return true;

		case COMPOST_ROTTED_FULL_OPEN:
		case COMPOST_ROTTED_OPEN:
			retrieveCompost(CompostBinLocations.forPosition(new Position(objectX, objectY)).getCompostIndex());
			return true;

		}
		return false;
	}

	/* handling the object click method */

	/* handle what happens when the player opens the compost bin */
	public void openCompostBin(final int index) {
		// check if the time elapsed is enough to rot the compost
		int timerRequired;
		timerRequired = compostBins[index] == 200 ? 90 : 45;
		if (World.getWorld().getUptime() - compostBinsTimer[index] >= timerRequired) {
			compostBins[index] += 50;
			player.playAnimation(new Animation(834, 0));
			World.getWorld().schedule(new ScheduledTask(2, false) {

				@Override
				public void execute() {
					updateCompostBin(index);
					stop();
				}

				@Override
				public void stop() {
					super.stop();
				}

			});
		} else
			player.sendMessage("The compost bin is still rotting. I should wait until it is complete.");
	}

	/* reseting the compost variables */

	public void resetVariables(int index) {
		compostBins[index] = 0;
		compostBinsTimer[index] = 0;
		organicItemAdded[index] = 0;
	}

	// handle what happens when the player retrieve the compost
	public void retrieveCompost(final int index) {
		final int finalItem = compostBins[index] == 150 ? COMPOST : compostBins[index] == 250 ? SUPER_COMPOST
				: ROTTE_TOMATO;

		player.playAnimation(new Animation(832, 0));
		final Action<Player> action = new Action<Player>(2, false, player) {

			@Override
			public void execute() {
				if (!player.getInventory().contains(1925) && compostBins[index] != 350 || organicItemAdded[index] == 0) {
					stop();
					return;
				}
				player.getSkillSet().addExperience(
						Skill.FARMING,
						finalItem == COMPOST ? COMPOST_EXP_RETRIEVE
								: finalItem == SUPER_COMPOST ? SUPER_COMPOST_EXP_RETRIEVE
										: ROTTEN_TOMATOES_EXP_RETRIEVE);
				if (compostBins[index] != 350)
					player.getInventory().remove(new Item(1925));
				player.getInventory().add(new Item(finalItem));
				player.playAnimation(new Animation(832, 0));
				organicItemAdded[index]--;
				if (organicItemAdded[index] == 0)
					resetVariables(index);
				updateCompostBin(index);
			}

			@Override
			public void stop() {
				player.stopAnimation();
				super.stop();
			}

		};

		player.startAction(action);
	}

	public void setCompostBins(int i, int compostBins) {
		this.compostBins[i] = compostBins;
	}

	public void setCompostBinsTimer(int i, long compostBinsTimer) {
		this.compostBinsTimer[i] = compostBinsTimer;
	}

	public void setOrganicItemAdded(int i, int organicItemAdded) {
		this.organicItemAdded[i] = organicItemAdded;
	}

	/* handle compost bin updating */
	private void updateCompostBin(int index) {
		updateCompostStates();
	}

	public void updateCompostStates() {
		// catherby north - catherby south - falador north west - falador south
		// east - phasmatys north west - phasmatys south east - ardougne north -
		// ardougne south
		final int[] configValues = new int[compostBins.length];

		int configValue;
		for (int i = 0; i < compostBins.length; i++)
			configValues[i] = getConfigValue(i);

		configValue = configValues[0] * 256 * 256 * 256 + configValues[1] * 256 * 256 + configValues[2] + configValues[3] * 256;
		player.send(new ConfigEvent(COMPOST_CONFIG, configValue));
	}

}
