package org.apollo.game.model.region;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.game.event.impl.MapEvent;
import org.apollo.game.event.impl.RegionUpdateEvent;
import org.apollo.game.model.Character;
import org.apollo.game.model.Entity;
import org.apollo.game.model.Npc;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.obj.DynamicGameObject;
import org.apollo.game.model.obj.GameObject;
import org.apollo.game.model.obj.StaticGameObject;
import org.apollo.util.EntityRepository;

/**
 * A region which consists of 8x8 chunks.
 * @author Steve
 */
public final class Region {

	/**
	 * The region size.
	 */
	public static final int SIZE = 64;

	/**
	 * The position of this region.
	 */
	private final Position position;

	/**
	 * The entity repository.
	 */
	private final EntityRepository<Entity> repository = new EntityRepository<Entity>();

	/**
	 * The region absolute position.
	 */
	private final Position regionPosition;

	/**
	 * Creates the new region.
	 * @param position The regions position.
	 */
	public Region(Position position) {
		this.position = position;
		this.regionPosition = new Position(position.getX() << 3 << 3, position.getY() << 3 << 3);
	}

	/**
	 * Adds an entity.
	 * @param entity The entity to add.
	 */
	public void add(Entity entity) {
		switch (entity.type()) {
		case Entity.DYNAMIC_OBJECT_TYPE:
		case Entity.GROUND_TYPE:
			// These types need to be transfered to a chunking system.
			getChunk(entity.getPosition()).add(entity);
			break;
		default:
			// The rest can be added to the entity repository.
			repository.add(entity);
			break;
		}
	}

	/**
	 * Adds a event.
	 * @param event The event.
	 * @deprecated instanceof, will be removed.
	 */
	@Deprecated
	public void add(Event event) {
		if (event instanceof MapEvent) {
			final Position position = ((MapEvent) event).getPosition();
			getChunk(position).add(event);
		}
	}

	/**
	 * Gets the characters.
	 * @return The characters.
	 */
	public final Collection<Character> getCharacters() {
		final List<Character> characters = new LinkedList<Character>();
		characters.addAll(repository.get(Player.class));
		characters.addAll(repository.get(Npc.class));
		return Collections.unmodifiableCollection(new LinkedList<Character>(characters));
	}

	/**
	 * Gets the chunk.
	 * @param position The position.
	 * @return The chunk.
	 */
	private Chunk getChunk(Position position) {
		final RegionManager regionManager = World.getWorld().getRegionManager();
		return regionManager.getChunkByPosition(position);
	}

	/**
	 * Gets the npcs.
	 * @return The npcs.
	 */
	public final Collection<Npc> getNpcs() {
		final List<Npc> list = repository.get(Entity.NPC_TYPE);
		return Collections.unmodifiableCollection(new LinkedList<Npc>(list));
	}

	/**
	 * Gets the object at the position.
	 * @param position The object at the position.
	 * @return The object at the position.
	 * @note this will only work for normal type 10 objects.
	 */
	public GameObject getObject(Position position) {
		return getObject(position, 10);
	}

	/**
	 * Gets the object at the position.
	 * @param position The object at the position.
	 * @param type The type of object.
	 * @return The object at the position.
	 */
	public GameObject getObject(Position position, int type) {
		final Chunk chunk = getChunk(position);
		final List<StaticGameObject> regionObjects = repository.get(position, StaticGameObject.class);
		final Collection<DynamicGameObject> chunkObjects = chunk.getObject(position);
		for (final DynamicGameObject object : chunkObjects)
			if (!object.isDeleting() || object.isReplacing())
				if (object.getType() == type || type < 0)
					return object;
		for (final StaticGameObject obj : regionObjects)
			if (obj.getType() == type || type < 0)
				return obj;
		return null;
	}

	/**
	 * Gets the players.
	 * @return The players.
	 */
	public final Collection<StaticGameObject> getObjects() {
		final List<StaticGameObject> list = repository.get(Entity.STATIC_OBJECT_TYPE);
		return Collections.unmodifiableCollection(new LinkedList<StaticGameObject>(list));
	}

	/**
	 * Gets the players.
	 * @return The players.
	 */
	public final Collection<Player> getPlayers() {
		final List<Player> list = repository.get(Entity.PLAYER_TYPE);
		return Collections.unmodifiableCollection(new LinkedList<Player>(list));
	}

	/**
	 * Gets the position.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the region position.
	 * @return The region position.
	 */
	public Position getRegionPosition() {
		return regionPosition;
	}

	/**
	 * Removes an entity.
	 * @param entity The entity.
	 */
	public void remove(Entity entity) {
		switch (entity.type()) {
		case Entity.DYNAMIC_OBJECT_TYPE:
		case Entity.GROUND_TYPE:
			getChunk(entity.getPosition()).remove(entity);
			break;
		default:
			repository.remove(entity);
			break;
		}
	}

	/**
	 * Updates this region's chunks.
	 */
	public void update() {
		for (final Player player : getPlayers())
			player.setRegionChanged(true);
	}

	/**
	 * Updates this region's chunk.
	 * @param chunk The chunk.
	 */
	public void update(Chunk chunk) {
		for (final Player player : getPlayers()) {
			final RegionUpdateEvent event = chunk.make(player);
			player.send(event);
			// Must call this whenever we send region events.
			player.getLocalEventList().addAll(event.getEvents());
		}
	}

}
