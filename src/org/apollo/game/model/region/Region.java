package org.apollo.game.model.region;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apollo.game.event.Event;
import org.apollo.game.event.impl.MapEvent;
import org.apollo.game.model.Character;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Npc;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.obj.DynamicGameObject;
import org.apollo.game.model.obj.GameObject;
import org.apollo.game.model.obj.StaticGameObject;

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
	 * The objects in this region.
	 */
	private final List<StaticGameObject> objects = new LinkedList<StaticGameObject>();

	/**
	 * The players in this region.
	 */
	private final List<Player> players = new LinkedList<Player>();

	/**
	 * The npcs in this region.
	 */
	private final List<Npc> npcs = new LinkedList<Npc>();

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
	 * Adds a character.
	 * @param character The character.
	 */
	public void add(Character character) {
		if (character.isControlling())
			add((Player) character);
		else
			add((Npc) character);
	}

	/**
	 * Adds a dynamic game object.
	 * @param object The dynamic game object.
	 */
	public void add(DynamicGameObject object) {
		getChunk(object.getPosition()).add(object);
	}

	/**
	 * Adds a event.
	 * @param event The event.
	 * @deprecated instanceof
	 */
	@Deprecated
	public void add(Event event) {
		if (event instanceof MapEvent) {
			final Position position = ((MapEvent) event).getPosition();
			getChunk(position).add(event);
		}
	}

	/**
	 * Adds a ground item.
	 * @param item The ground item.
	 */
	public void add(GroundItem item) {
		getChunk(item.getPosition()).add(item);
	}

	/**
	 * Adds a npc.
	 * @param npc The npc.
	 */
	public void add(Npc npc) {
		npcs.add(npc);
	}

	/**
	 * Adds a player.
	 * @param player The player.
	 */
	public void add(Player player) {
		players.add(player);
	}

	/**
	 * Adds a static object.
	 * @param object The static object.
	 */
	public void add(StaticGameObject object) {
		objects.add(object);
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
		return Collections.unmodifiableCollection(new LinkedList<Npc>(npcs));
	}
	
	/**
	 * Gets the characters.
	 * @return The characters.
	 */
	public final Collection<Character> getCharacters() {
		final List<Character> characters = new LinkedList<Character>();
		characters.addAll(players);
		characters.addAll(npcs);
		return Collections.unmodifiableCollection(new LinkedList<Character>(characters));
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
		final Collection<DynamicGameObject> objects = getChunk(position).getObjects();
		for (final DynamicGameObject object : objects)
			if (!object.isDeleting() || object.isReplacing())
				if (object.getType() == type)
					if (object.getPosition().equals(position))
						return object;
		for (final StaticGameObject object : this.objects)
			if (object.getPosition().equals(position))
				if (object.getType() == type)
					return object;
		return null;
	}

	/**
	 * Gets the players.
	 * @return The players.
	 */
	public Collection<StaticGameObject> getObjects() {
		return Collections.unmodifiableCollection(new LinkedList<StaticGameObject>(objects));
	}

	/**
	 * Gets the players.
	 * @return The players.
	 */
	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(new LinkedList<Player>(players));
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
	 * Removes a character.
	 * @param character The character.
	 */
	public void remove(Character character) {
		if (character.isControlling())
			remove((Player) character);
		else
			remove((Npc) character);
	}

	/**
	 * Removes a item.
	 * @param item The item.
	 */
	public void remove(GroundItem item) {
		getChunk(item.getPosition()).remove(item);
	}

	/**
	 * Removes a npc.
	 * @param npc The npc.
	 */
	public void remove(Npc npc) {
		npcs.remove(npc);
	}

	/**
	 * Removes a player.
	 * @param player The player.
	 */
	public void remove(Player player) {
		players.remove(player);
	}

	/**
	 * Updates this region's chunks.
	 */
	public void update() {
		for (final Player player : players)
			player.setRegionChanged(true);
	}

	/**
	 * Updates this region's chunk.
	 * @param chunk The chunk.
	 */
	public void update(Chunk chunk) {
		for (final Player player : players)
			player.send(chunk.make(player));
	}

}
