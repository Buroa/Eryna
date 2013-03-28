package org.apollo.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apollo.game.model.Character;
import org.apollo.game.model.Entity;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Npc;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.obj.DynamicGameObject;
import org.apollo.game.model.obj.StaticGameObject;

/**
 * A {@link EntityRepository} is a repository of {@link Character}s that are
 * currently active in the game world.
 * @param <T> The type of entity.
 * @author Steve
 */
public final class EntityRepository<T extends Entity> {

	/**
	 * The entitys.
	 */
	private final Map<Position, List<T>> entitys = new HashMap<Position, List<T>>();
	
	/**
	 * The players.
	 */
	private final List<Player> players = new LinkedList<Player>();
	
	/**
	 * The npcs.
	 */
	private final List<Npc> npcs = new LinkedList<Npc>();
	
	/**
	 * The static objects.
	 */
	private final List<StaticGameObject> staticObjects = new LinkedList<StaticGameObject>();
	
	/**
	 * The dynamic objects.
	 */
	private final List<DynamicGameObject> dynamicObjects = new LinkedList<DynamicGameObject>();
	
	/**
	 * The grounds.
	 */
	private final List<GroundItem> grounds = new LinkedList<GroundItem>();
	
	/**
	 * Adds a entity to the repository.
	 * @param entity The entity to add.
	 * @return {@code true} if the entity was added, {@code false} if the
	 * entity has already been added.
	 */
	public boolean add(T entity) {
		boolean add_entity = _add(entity);
		if (!add_entity)
			return false;
		switch(entity.type()) {
		case Entity.PLAYER_TYPE:
			return players.add((Player) entity);
		case Entity.NPC_TYPE:
			return npcs.add((Npc) entity);
		case Entity.STATIC_OBJECT_TYPE:
			return staticObjects.add((StaticGameObject) entity);
		case Entity.DYNAMIC_OBJECT_TYPE:
			return dynamicObjects.add((DynamicGameObject) entity);
		case Entity.GROUND_TYPE:
			return grounds.add((GroundItem) entity);
		default:
			return add_entity;
		}
	}
	
	/**
	 * Removes a entity from the repository.
	 * @param entity The entity to remove.
	 * @return {@code true} if the entity was removed, {@code false} if the
	 * entity was not found.
	 */
	public boolean remove(T entity) {
		boolean remove_entity = _remove(entity);
		switch(entity.type()) {
		case Entity.PLAYER_TYPE:
			return players.remove((Player) entity) & remove_entity;
		case Entity.NPC_TYPE:
			return npcs.remove((Npc) entity) & remove_entity;
		case Entity.STATIC_OBJECT_TYPE:
			return staticObjects.remove((StaticGameObject) entity) & remove_entity;
		case Entity.DYNAMIC_OBJECT_TYPE:
			return dynamicObjects.remove((DynamicGameObject) entity) & remove_entity;
		case Entity.GROUND_TYPE:
			return grounds.remove((GroundItem) entity) & remove_entity;
		default:
			return remove_entity;
		}
	}
	
	/**
	 * Gets an entity from the repository.
	 * @param type The entity type.
	 * @return The entitys components.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Entity> List<E> get(int type) {
		switch(type) {
		case Entity.PLAYER_TYPE:
			return (List<E>) players;
		case Entity.NPC_TYPE:
			return (List<E>) npcs;
		case Entity.STATIC_OBJECT_TYPE:
			return (List<E>) staticObjects;
		case Entity.DYNAMIC_OBJECT_TYPE:
			return (List<E>) dynamicObjects;
		case Entity.GROUND_TYPE:
			return (List<E>) grounds;
		default:
			return null;
		}
	}
	
	/**
	 * Gets an entity from the repository.
	 * @param entity The entity.
	 * @return The entitys components.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Entity> List<E> get(Class<E> entity) {
		if (entity.equals(Player.class))
			return (List<E>) players;
		if (entity.equals(Npc.class))
			return (List<E>) npcs;
		if (entity.equals(StaticGameObject.class))
			return (List<E>) staticObjects;
		if (entity.equals(DynamicGameObject.class))
			return (List<E>) dynamicObjects;
		if (entity.equals(GroundItem.class))
			return (List<E>) grounds;
		return null;
	}
	
	/**
	 * Gets the entitys at the position.
	 * @param position The position.
	 * @param clazz The clazz of the entitys to search for.
	 * @return The list of the entitys.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Entity> List<E> get(Position position, Class<E> clazz) {
		final LinkedList<E> list = new LinkedList<E>();
		final List<T> entitys = this.entitys.get(position);
		if (entitys != null) {
			for (T entity : entitys) {
				if (entity.getClass().equals(clazz))
					list.add((E) entity);
			}
		}
		return list;
	}
	
	/**
	 * Adds an entity.
	 * @param entity The entity to add.
	 * @return True if the entity was added, false if otherwise.
	 */
	private boolean _add(T entity) {
		List<T> entitys = this.entitys.get(entity.getPosition());
		if (entitys == null) {
			entitys = new LinkedList<T>();
			this.entitys.put(entity.getPosition(), entitys);
		}
		if (!entitys.contains(entity))
			return entitys.add(entity);
		return false;
	}
	
	/**
	 * Removes an entity.
	 * @param entity The entity to remove.
	 * @return True if the entity was removed, false if otherwise.
	 */
	private boolean _remove(T entity) {
		List<T> entitys = this.entitys.get(entity.getPosition());
		if (entitys != null)
			return entitys.remove(entity);
		return false;
	}
	
	@Override
	public String toString() {
		return EntityRepository.class.getName() + " [players=" + players + ", npcs=" + npcs + ", static_o=" + staticObjects + ", dynamic_o=" + dynamicObjects + ", grounds=" + grounds + "]";
	}
}
