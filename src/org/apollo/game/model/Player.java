package org.apollo.game.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apollo.game.event.Event;
import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.game.model.Inventory.StackMode;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.AppearanceInventoryListener;
import org.apollo.game.model.inv.BonusesEquipmentListener;
import org.apollo.game.model.inv.FullInventoryListener;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.RunecraftingEquipmentListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;
import org.apollo.game.model.melee.Prayer;
import org.apollo.game.model.skill.LevelUpSkillListener;
import org.apollo.game.model.skill.PrayerSkillListener;
import org.apollo.game.model.skill.SkillListener;
import org.apollo.game.model.skill.SynchronizationSkillListener;
import org.apollo.game.model.skill.farming.FarmingSet;
import org.apollo.game.scheduling.impl.FarmingScheduledTask;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.net.session.GameSession;
import org.apollo.security.PlayerCredentials;

/**
 * A {@link Player} is a {@link Character} that a user is controlling.
 * @author Graham
 */
public final class Player extends Character {

	/**
	 * An enumeration with the different privilege levels a player can have.
	 * @author Graham
	 */
	public enum PrivilegeLevel {
		/**
		 * A standard (rights 0) account.
		 */
		STANDARD(0),
		/**
		 * A player member (rights 1) account.
		 */
		MEMBER(1),
		/**
		 * A player moderator (rights 2) account.
		 */
		MODERATOR(2),
		/**
		 * A player administrator (rights 3) account.
		 */
		ADMINISTRATOR(3),
		/**
		 * A player developer (rights 4) account.
		 */
		DEVELOPER(4),
		/**
		 * A owner (rights 5) account.
		 */
		OWNER(5);

		/**
		 * Gets the privilege level for the specified numerical level.
		 * @param numericalLevel The numerical level.
		 * @return The privilege level.
		 */
		public static PrivilegeLevel valueOf(int numericalLevel) {
			for (final PrivilegeLevel level : values())
				if (level.numericalLevel == numericalLevel)
					return level;
			throw new IllegalArgumentException("invalid numerical level");
		}

		/**
		 * The numerical level used in the protocol.
		 */
		private final int numericalLevel;

		/**
		 * Creates a privilege level.
		 * @param numericalLevel The numerical level.
		 */
		private PrivilegeLevel(int numericalLevel) {
			this.numericalLevel = numericalLevel;
		}

		/**
		 * Gets the numerical level.
		 * @return The numerical level used in the protocol.
		 */
		public int toInteger() {
			return numericalLevel;
		}
	}

	/**
	 * A temporary queue of events sent during the login process.
	 */
	private final Queue<Event> queuedEvents = new ArrayDeque<Event>();

	/**
	 * A list of local players.
	 */
	private final List<Player> localPlayers = new ArrayList<Player>();

	/**
	 * A list of local NPCs.
	 */
	private final List<Npc> localNPCs = new ArrayList<Npc>();

	/**
	 * A list of local events.
	 */
	private final List<Event> localEvents = new ArrayList<Event>();

	/**
	 * The player's credentials.
	 */
	private final PlayerCredentials credentials;

	/**
	 * The privilege level.
	 */
	private PrivilegeLevel privilegeLevel = PrivilegeLevel.STANDARD;

	/**
	 * The {@link GameSession} currently attached to this {@link Player}.
	 */
	private GameSession session;

	/**
	 * The center of the last region the client has loaded.
	 */
	private Position lastKnownRegion;

	/**
	 * A flag indicating if the region changed in the last cycle.
	 */
	private boolean regionChanged = false;

	/**
	 * The player's appearance.
	 */
	private Appearance appearance = Appearance.DEFAULT_APPEARANCE;

	/**
	 * The current maximum viewing distance of this player.
	 */
	private int viewingDistance = 1;

	/**
	 * A flag which indicates there are players that couldn't be added.
	 */
	private boolean excessivePlayers = false;

	/**
	 * This player's interface set.
	 */
	private final InterfaceSet interfaceSet = new InterfaceSet(this);

	/**
	 * The players settings.
	 */
	private final PlayerSettings settings = new PlayerSettings();

	/**
	 * The player's deposit box.
	 */
	private final Inventory depositBox = new Inventory(InventoryConstants.INVENTORY_CAPACITY,
			StackMode.STACK_STACKABLE_ITEMS);

	/**
	 * The frenemy set.
	 */
	private final FrenemySet frenemySet = new FrenemySet(this);

	/**
	 * The combat interface special.
	 */
	// private int specialInterface = -1;

	/**
	 * Creates the {@link Player}.
	 * @param credentials The player's credentials.
	 * @param position The initial position.
	 */
	public Player(PlayerCredentials credentials, Position position) {
		super(position);
		init();
		this.credentials = credentials;
	}

	/**
	 * Decrements this player's viewing distance if it is greater than 1.
	 */
	public void decrementViewingDistance() {
		if (viewingDistance > 1)
			viewingDistance--;
	}

	/**
	 * Sends the exit player events.
	 */
	@Override
	public void exit() {
	}

	/**
	 * Sets the excessive players flag.
	 */
	public void flagExcessivePlayers() {
		excessivePlayers = true;
	}

	/**
	 * Gets the player's appearance.
	 * @return The appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Gets the player's credentials.
	 * @return The player's credentials.
	 */
	public PlayerCredentials getCredentials() {
		return credentials;
	}

	/**
	 * Gets the player's deposit box.
	 * @return The player's deposit box.
	 */
	public Inventory getDepositBox() {
		return depositBox;
	}

	/**
	 * Gets the player's name, encoded as a long.
	 * @return The encoded player name.
	 */
	public long getEncodedName() {
		return credentials.getEncodedUsername();
	}

	/**
	 * Gets the frenemy set.
	 * @return The frenemy set.
	 */
	public FrenemySet getFrenemySet() {
		return frenemySet;
	}

	/**
	 * Gets this player's interface set.
	 * @return The interface set for this player.
	 */
	public InterfaceSet getInterfaceSet() {
		return interfaceSet;
	}

	/**
	 * Gets the last known region.
	 * @return The last known region, or {@code null} if the player has never
	 * known a region.
	 */
	public Position getLastKnownRegion() {
		return lastKnownRegion;
	}

	/**
	 * Gets the local event list.
	 * @return The local event list.
	 */
	public List<Event> getLocalEventList() {
		return localEvents;
	}

	/**
	 * Gets the local NPC list.
	 * @return The local NPC list.
	 */
	public List<Npc> getLocalNpcList() {
		return localNPCs;
	}

	/**
	 * Gets the local player list.
	 * @return The local player list.
	 */
	public List<Player> getLocalPlayerList() {
		return localPlayers;
	}

	/**
	 * Gets the player's name.
	 * @return The player's name.
	 */
	public String getName() {
		return credentials.getUsername();
	}

	/**
	 * Gets the privilege level.
	 * @return The privilege level.
	 */
	public PrivilegeLevel getPrivilegeLevel() {
		return privilegeLevel;
	}

	/**
	 * Gets the game session.
	 * @return The game session.
	 */
	public GameSession getSession() {
		return session;
	}

	/**
	 * Gets the player settings.
	 * @return The player settings.
	 */
	public PlayerSettings getSettings() {
		return settings;
	}

	/**
	 * Gets this player's viewing distance.
	 * @return The viewing distance.
	 */
	public int getViewingDistance() {
		return viewingDistance;
	}

	/**
	 * Checks if this player has ever known a region.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean hasLastKnownRegion() {
		return lastKnownRegion != null;
	}

	/**
	 * Checks if the region has changed.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean hasRegionChanged() {
		return regionChanged;
	}

	/**
	 * Increments this player's viewing distance if it is less than the maximum
	 * viewing distance.
	 */
	public void incrementViewingDistance() {
		if (viewingDistance < Position.MAX_DISTANCE)
			viewingDistance++;
	}

	/**
	 * Initialises this player.
	 */
	private void init() {
		initInventories();
		initSkills();
	}

	/**
	 * Initializes the player's inventories.
	 */
	private void initInventories() {
		final Inventory inventory = getInventory();
		final Inventory bank = getBank();
		final Inventory equipment = getEquipment();
		// TODO only add bank listener when it is open? (like Hyperion)
		// inventory full listeners
		final InventoryListener fullInventoryListener = new FullInventoryListener(this,
				FullInventoryListener.FULL_INVENTORY_MESSAGE);
		final InventoryListener fullBankListener = new FullInventoryListener(this,
				FullInventoryListener.FULL_BANK_MESSAGE);
		final InventoryListener fullEquipmentListener = new FullInventoryListener(this,
				FullInventoryListener.FULL_EQUIPMENT_MESSAGE);
		// equipment appearance listener
		final InventoryListener appearanceListener = new AppearanceInventoryListener(this);
		// synchronization listeners
		final InventoryListener syncInventoryListener = new SynchronizationInventoryListener(this,
				SynchronizationInventoryListener.INVENTORY_ID);
		final InventoryListener syncBankListener = new SynchronizationInventoryListener(this,
				BankConstants.BANK_INVENTORY_ID);
		final InventoryListener syncEquipmentListener = new SynchronizationInventoryListener(this,
				SynchronizationInventoryListener.EQUIPMENT_ID);
		final InventoryListener equipmentBonusesListener = new BonusesEquipmentListener(this);
		final InventoryListener runecraftingEquipmentListener = new RunecraftingEquipmentListener(this);
		// add the listeners
		inventory.addListener(syncInventoryListener);
		inventory.addListener(fullInventoryListener);
		bank.addListener(syncBankListener);
		bank.addListener(fullBankListener);
		equipment.addListener(syncEquipmentListener);
		equipment.addListener(appearanceListener);
		equipment.addListener(fullEquipmentListener);
		equipment.addListener(equipmentBonusesListener);
		equipment.addListener(runecraftingEquipmentListener);
	}

	/**
	 * Initializes the player's skills.
	 */
	private void initSkills() {
		final SkillSet skills = getSkillSet();
		// synchronization listener
		final SkillListener syncListener = new SynchronizationSkillListener(this);
		// level up listener
		final SkillListener levelUpListener = new LevelUpSkillListener(this);
		// prayer listener
		final SkillListener prayerListener = new PrayerSkillListener(this);
		// add the listeners
		skills.addListener(syncListener);
		skills.addListener(levelUpListener);
		skills.addListener(prayerListener);
	}

	@Override
	public boolean isControlling() {
		return true;
	}

	/**
	 * Checks if there are excessive players.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isExcessivePlayersSet() {
		return excessivePlayers;
	}

	/**
	 * Gets the hide boolean flag.
	 * @return True if hidden, false if otherwise.
	 */
	public boolean isHidden() {
		return settings.isHidden() && privilegeLevel.equals(PrivilegeLevel.DEVELOPER);
	}

	/**
	 * Logs the player out, if possible.
	 */
	public void logout() {
		send(new LogoutEvent());
	}

	@Override
	public void pulse() {
		Prayer.drainPrayer(this);
	}

	/**
	 * Resets the excessive players flag.
	 */
	public void resetExcessivePlayers() {
		excessivePlayers = false;
	}

	/**
	 * Resets this player's viewing distance.
	 */
	public void resetViewingDistance() {
		viewingDistance = 1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.game.model.Character#send(org.apollo.game.event.Event)
	 */
	@Override
	public void send(org.apollo.game.event.Event event) {
		if (isActive()) {
			if (!queuedEvents.isEmpty()) {
				for (final Event queuedEvent : queuedEvents)
					session.dispatchEvent(queuedEvent);
				queuedEvents.clear();
			}
			session.dispatchEvent(event);
		} else
			queuedEvents.add(event);
	}

	/**
	 * Sets the player's appearance.
	 * @param appearance The new appearance.
	 */
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
		this.getBlockSet().add(SynchronizationBlock.createAppearanceBlock(this));
	}

	/**
	 * Sets the last known region.
	 * @param lastKnownRegion The last known region.
	 */
	public void setLastKnownRegion(Position lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
	}

	/**
	 * Changes the membership status of this player.
	 * @param members The new membership flag.
	 */
	public void setMembers(boolean members) {
		settings.setMembers(members);
		if (members)
			if (privilegeLevel.toInteger() < PrivilegeLevel.MEMBER.toInteger())
				privilegeLevel = PrivilegeLevel.MEMBER;
	}

	/**
	 * Sets the privilege level.
	 * @param privilegeLevel The privilege level.
	 */
	public void setPrivilegeLevel(PrivilegeLevel privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}

	/**
	 * Sets the region changed flag.
	 * @param regionChanged A flag indicating if the region has changed.
	 */
	public void setRegionChanged(boolean regionChanged) {
		this.regionChanged = regionChanged;
	}

	/**
	 * Sets the player's {@link GameSession}.
	 * @param session The player's {@link GameSession}.
	 * @param reconnecting The reconnecting flag.
	 */
	public void setSession(GameSession session, boolean reconnecting) {
		this.session = session;
		getBlockSet().add(SynchronizationBlock.createAppearanceBlock(this));
	}

	/**
	 * Sets the farming processing.
	 */
	public void startFarming() {
		if (!settings.isFarming()) {
			settings.setFarmingSet(new FarmingSet(this));
			World.getWorld().schedule(new FarmingScheduledTask(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Player.class.getName() + " [username=" + credentials.getUsername() + ", privilegeLevel="
				+ privilegeLevel + "]";
	}
}
