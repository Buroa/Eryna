package org.apollo.game.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apollo.ServerContext;
import org.apollo.Service;
import org.apollo.fs.FileSystemConstants;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.ItemDefinitionParser;
import org.apollo.fs.parser.NpcDefinitionParser;
import org.apollo.fs.parser.ObjectDefinitionParser;
import org.apollo.fs.parser.StaticObjectDefinitionParser;
import org.apollo.game.command.CommandDispatcher;
import org.apollo.game.event.impl.DestroyObjectEvent;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.inter.store.WorldStore;
import org.apollo.game.model.obj.DynamicGameObject;
import org.apollo.game.model.obj.GameObject;
import org.apollo.game.model.region.RegionManager;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.Scheduler;
import org.apollo.game.scheduling.impl.UptimeTask;
import org.apollo.io.EquipmentDefinitionParser;
import org.apollo.io.NpcSkillSetParser;
import org.apollo.io.NpcSpawnParser;
import org.apollo.io.ObjectSpawnParser;
import org.apollo.io.player.PlayerListener;
import org.apollo.net.release.Release;
import org.apollo.util.CharacterRepository;
import org.apollo.util.plugin.PluginManager;
import org.apollo.world.WorldDispatcher;

/**
 * The world class is a singleton which contains objects like the
 * {@link CharacterRepository} for players and NPCs. It
 * should only contain things relevant to the in-game world and not classes
 * which deal with I/O and such (these may be
 * better off inside some custom {@link Service} or other code, however, the
 * circumstances are rare).
 * @author Graham
 */
public final class World {

	/**
	 * Represents the different status codes for registering a player.
	 * @author Graham
	 */
	public enum RegistrationStatus {
		/**
		 * Indicates the world is full.
		 */
		WORLD_FULL,
		/**
		 * Indicates the world is offline.
		 */
		WORLD_OFFLINE,
		/**
		 * Indicates the world is being updated.
		 */
		WORLD_UPDATING,
		/**
		 * Indicates that the player is already online.
		 */
		ALREADY_ONLINE,
		/**
		 * Indicates that the player was registered successfully.
		 */
		OK;
	}

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(World.class.getName());

	/**
	 * The world.
	 */
	private static final World world = new World();

	/**
	 * The world shops.
	 */
	private static final WorldStore worldStore = new WorldStore();

	/**
	 * Gets the world.
	 * @return The world.
	 */
	public static World getWorld() {
		return world;
	}

	/**
	 * The scheduler.
	 */
	private final Scheduler scheduler = new Scheduler();

	/**
	 * The command dispatcher.
	 */
	private final CommandDispatcher dispatcher = new CommandDispatcher();

	/**
	 * The world dispatcher.
	 */
	private final WorldDispatcher worldDispatcher = new WorldDispatcher();

	/**
	 * The plugin manager.
	 */
	private PluginManager pluginManager;

	/**
	 * The {@link CharacterRepository} of {@link Player}s.
	 */
	private final CharacterRepository<Player> playerRepository = new CharacterRepository<Player>(WorldConstants.MAXIMUM_PLAYERS);

	/**
	 * The {@link CharacterRepository} of {@link Npc}s.
	 */
	private final CharacterRepository<Npc> npcRepository = new CharacterRepository<Npc>(WorldConstants.MAXIMUM_NPCS);

	/**
	 * The list of ground items.
	 */
	private final List<GroundItem> items = new ArrayList<GroundItem>();

	/**
	 * The region manager.
	 */
	private final RegionManager regionManager = new RegionManager();

	/**
	 * The uptime.
	 */
	private int uptime;

	/**
	 * The player listeners.
	 */
	private final List<PlayerListener> playerListeners = new ArrayList<PlayerListener>();

	/**
	 * The current release.
	 */
	private Release release;

	/**
	 * The crc table. TODO move
	 */
	private int[] crcs = new int[FileSystemConstants.ARCHIVE_COUNT];

	/**
	 * Creates the world.
	 */
	private World() {
		schedule(new UptimeTask());
	}

	/**
	 * Gets the command dispatcher. TODO should this be here?
	 * @return The command dispatcher.
	 */
	public CommandDispatcher getCommandDispatcher() {
		return dispatcher;
	}

	/**
	 * Gets the items.
	 * @return The items.
	 */
	public Collection<GroundItem> getItems() {
		return Collections.unmodifiableCollection(new LinkedList<GroundItem>(items));
	}

	/**
	 * Gets the NPC repository.
	 * @return The NPC repository.
	 */
	public CharacterRepository<Npc> getNpcRepository() {
		return npcRepository;
	}

	/**
	 * Gets the object at the position.
	 * @param position The position.
	 * @return The game object, if any, at the position.
	 */
	public GameObject getObject(Position position) {
		; // TODO
		return null;
	}

	/**
	 * Gets the specified player.
	 * @param name The player's name.
	 * @return player The player.
	 */
	public Player getPlayer(String name) {
		for (final Player player : getPlayerRepository())
			if (player.getName().equalsIgnoreCase(name))
				return player;
		return null;
	}

	/**
	 * Gets the player listeners.
	 * @return The player listeners.
	 */
	public final List<PlayerListener> getPlayerListeners() {
		return playerListeners;
	}

	/**
	 * Gets the character repository. NOTE:
	 * {@link CharacterRepository#add(Character)} and
	 * {@link CharacterRepository#remove(Character)} should not be called
	 * directly! These mutation methods are not
	 * guaranteed to work in future releases!
	 * <p>
	 * Instead, use the {@link World#register(Player)} and
	 * {@link World#unregister(Player)} methods which do the same thing and will
	 * continue to work as normal in future releases.
	 * @return The character repository.
	 */
	public CharacterRepository<Player> getPlayerRepository() {
		return playerRepository;
	}

	/**
	 * Gets the plugin manager. TODO should this be here?
	 * @return The plugin manager.
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	/**
	 * Gets the region manager.
	 * @return The region manager.
	 */
	public RegionManager getRegionManager() {
		return regionManager;
	}

	/**
	 * Gets the release.
	 * @return The release initialized.
	 */
	public Release getRelease() {
		return release;
	}

	/**
	 * Gets the world stores.
	 * @return The world stores.
	 */
	public WorldStore getStores() {
		return worldStore;
	}

	/**
	 * Gets the uptime.
	 * @return The uptime.
	 */
	public long getUptime() {
		return uptime;
	}

	/**
	 * Gets the world dispatcher.
	 * @return The world dispatcher.
	 */
	public WorldDispatcher getWorldDispatcher() {
		return worldDispatcher;
	}

	/**
	 * Initialises the world by loading definitions from the specified file
	 * system.
	 * @param release The release number.
	 * @param fs The file system.
	 * @param mgr The plugin manager. TODO move this.
	 * @param context The server context.
	 * @throws IOException if an I/O error occurs.
	 */
	public void init(int release, IndexedFileSystem fs, PluginManager mgr, ServerContext context) throws IOException {
		final ByteBuffer crcTable = fs.getCrcTable();
		for (int i = 0; i < crcs.length; i++)
			crcs[i] = crcTable.getInt();
		
		logger.info("Loading item definitions...");
		final ItemDefinitionParser itemParser = new ItemDefinitionParser(fs);
		final ItemDefinition[] itemDefs = itemParser.parse();
		ItemDefinition.init(itemDefs);
		logger.info("Done (loaded " + itemDefs.length + " item definitions).");

		logger.info("Loading equipment definitions...");
		int nonNull = 0;
		InputStream is = new BufferedInputStream(new FileInputStream("data/equipment-" + release + ".dat"));
		try {
			final EquipmentDefinitionParser equipParser = new EquipmentDefinitionParser(is);
			final EquipmentDefinition[] equipDefs = equipParser.parse();
			for (final EquipmentDefinition def : equipDefs)
				if (def != null)
					nonNull++;
			EquipmentDefinition.init(equipDefs);
		} finally {
			is.close();
		}
		logger.info("Done (loaded " + nonNull + " equipment definitions).");

		logger.info("Loading object definitions...");
		final ObjectDefinitionParser objectParser = new ObjectDefinitionParser(fs);
		final ObjectDefinition[] objectDefs = objectParser.parse();
		ObjectDefinition.init(objectDefs);
		logger.info("Done (loaded " + objectDefs.length + " object definitions).");

		logger.info("Loading static object definitions...");
		final StaticObjectDefinitionParser staticObjectParser = new StaticObjectDefinitionParser(fs);
		final int[] clipped = staticObjectParser.parse();
		logger.info("Done (loaded " + clipped[0] + " landscapes, " + clipped[1] + " tiles).");

		logger.info("Loading NPC definitions...");
		final NpcDefinitionParser npcDefParser = new NpcDefinitionParser(fs);
		final NpcDefinition[] npcDefs = npcDefParser.parse();
		NpcDefinition.init(npcDefs);
		logger.info("Done (loaded " + npcDefs.length + " NPC definitions).");

		logger.info("Loading NPC skills...");
		is = new FileInputStream("data/npc-skills.xml");
		final NpcSkillSetParser npcSkillSetParser = new NpcSkillSetParser(is);
		final int parsed = npcSkillSetParser.parse();
		logger.info("Done (loaded " + parsed + " NPC skills).");

		logger.info("Loading NPC spawns...");
		is = new FileInputStream("data/npc-spawns.xml");
		final NpcSpawnParser npcParser = new NpcSpawnParser(is);
		final Npc[] npcSpawns = npcParser.parse();
		for (final Npc npc : npcSpawns)
			register(npc);
		is.close();
		logger.info("Done (loaded " + npcSpawns.length + " NPC spawns).");

		logger.info("Loading Object spawns...");
		is = new FileInputStream("data/object-spawns.xml");
		final ObjectSpawnParser gameObjectParser = new ObjectSpawnParser(is);
		final DynamicGameObject[] objectSpawns = gameObjectParser.parse();
		for (final DynamicGameObject go : objectSpawns) {
			if (go.isDeleting()) {
				regionManager.getChunkByPosition(go.getPosition()).add(new DestroyObjectEvent(go));
				if (!go.isReplacing())
					continue;
			}
			register(go);
		}
		is.close();
		logger.info("Done (loaded " + objectSpawns.length + " Object spawns).");

		this.pluginManager = mgr;
		this.release = context.getRelease();
	}

	/**
	 * Checks if the specified player is online.
	 * @param name The player's name.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isPlayerOnline(String name) {
		for (final Player player : playerRepository)
			if (player.getName().equalsIgnoreCase(name))
				return true;
		return false;
	}

	/**
	 * Calls the {@link Scheduler#pulse()} method.
	 */
	public void pulse() {
		scheduler.pulse();
	}

	/**
	 * Registers an game object.
	 * @param object The game object.
	 */
	public void register(final DynamicGameObject object) {
		regionManager.getChunkByPosition(object.getPosition()).add(object);
	}

	/**
	 * Registers an ground item.
	 * @param item The ground item.
	 */
	public void register(final GroundItem item) {
		if (items.add(item))
			regionManager.getChunkByPosition(item.getPosition()).add(item);
	}

	/**
	 * Registers an NPC.
	 * @param npc The NPC.
	 */
	public void register(final Npc npc) {
		if (npcRepository.add(npc))
			regionManager.getRegionByPosition(npc.getPosition()).add(npc);
		else
			logger.info("Failed to register npc (server full): " + npc + " [online=" + npcRepository.size() + "]");
	}

	/**
	 * Registers the specified player.
	 * @param player The player.
	 * @return A {@link RegistrationStatus}.
	 */
	public RegistrationStatus register(final Player player) {
		if (isPlayerOnline(player.getName()))
			return RegistrationStatus.ALREADY_ONLINE;
		else {
			final boolean success = playerRepository.add(player);
			if (success) {
				logger.info("Registered player: " + player + " [online=" + playerRepository.size() + "]");
				return RegistrationStatus.OK;
			}
			else {
				logger.warning("Failed to register player (server full): " + player + " [online="
						+ playerRepository.size() + "]");
				return RegistrationStatus.WORLD_FULL;
			}
		}
	}

	/**
	 * Registers a player listener.
	 * @param listener The listener.
	 */
	public void register(PlayerListener listener) {
		playerListeners.add(listener);
	}

	/**
	 * Replaces an object at the position.
	 * @param position The position of the old object.
	 * @param object The object to replace with.
	 */
	public void replaceObject(Position position, GameObject object) {
		; // TODO
	}

	/**
	 * Schedules a new task.
	 * @param task The {@link ScheduledTask}.
	 */
	public void schedule(ScheduledTask task) {
		scheduler.schedule(task);
	}

	/**
	 * Sets the uptime.
	 * @param uptime The uptime to set.
	 */
	public void setUptime(int uptime) {
		this.uptime = uptime;
	}

	/**
	 * Unregisters an game object.
	 * @param object The game object.
	 */
	public void unregister(DynamicGameObject object) {
		regionManager.getChunkByPosition(object.getPosition()).remove(object);
	}

	/**
	 * Unregisters an ground item.
	 * @param item The ground item.
	 */
	public void unregister(GroundItem item) {
		if (items.remove(item))
			regionManager.getRegionByPosition(item.getPosition()).remove(item);
	}

	/**
	 * Unregisters the specified npc.
	 * @param npc The npc.
	 */
	public void unregister(Npc npc) {
		if (npcRepository.remove(npc)) {
			npc.exit();
			regionManager.getRegionByPosition(npc.getPosition()).remove(npc);
		} else
			logger.warning("Could not find npc to unregister: " + npc + "!");
	}

	/**
	 * Unregisters the specified player.
	 * @param player The player.
	 */
	public void unregister(Player player) {
		if (playerRepository.remove(player)) {
			for (final PlayerListener listener : playerListeners)
				try {
					listener.logout(player);
				} catch (final Exception e) {
					continue;
				}
			regionManager.getRegionByPosition(player.getPosition()).remove(player);
			logger.info("Unregistered player: " + player + " [online=" + playerRepository.size() + "]");
		} else
			logger.warning("Could not find player to unregister: " + player + "!");
	}

	/**
	 * Unregisteres a player listener.
	 * @param listener The player listener to unregister.
	 */
	public void unregister(PlayerListener listener) {
		playerListeners.remove(listener);
	}

	/**
	 * Gets the crc table.
	 * @return The crc table.
	 */
	public int[] getCrcs() {
		return crcs;
	}
}