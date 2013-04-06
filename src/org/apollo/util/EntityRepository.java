package org.apollo.util;

import java.util.ArrayList;
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
	 * The list of acceptors.
	 */
	private final List<Class<T>> acceptors = new ArrayList<Class<T>>(5);

	/**
	 * The entitys.
	 */
	private final Map<Position, List<T>> entitys = new HashMap<Position, List<T>>();

	/**
	 * The players.
	 */
	private List<Player> players;

	/**
	 * The npcs.
	 */
	private List<Npc> npcs;

	/**
	 * The static objects.
	 */
	private List<StaticGameObject> staticObjects;

	/**
	 * The dynamic objects.
	 */
	private List<DynamicGameObject> dynamicObjects;

	/**
	 * The grounds.
	 */
	private List<GroundItem> grounds;

	/**
	 * Adds an entity.
	 * @param entity The entity to add.
	 * @return True if the entity was added, false if otherwise.
	 */
	private boolean _add(T entity) {
		checkAcceptors(entity);
		List<T> entitys = this.entitys.get(entity.getPosition());
		if (entitys == null) {
			entitys = new ArrayList<T>();
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
		checkAcceptors(entity);
		final List<T> entitys = this.entitys.get(entity.getPosition());
		if (entitys != null)
			return entitys.remove(entity);
		return false;
	}

	/**
	 * Creates the entity repository.
	 * @param acceptors The acceptors.
	 * @return The region repository.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EntityRepository accept(Class... acceptors) {
		for (final Class acceptor : acceptors) {
			this.acceptors.add(acceptor);
			if (acceptor.equals(Player.class) && players == null)
				players = new ArrayList<Player>();
			else if (acceptor.equals(Npc.class) && npcs == null)
				npcs = new ArrayList<Npc>();
			else if (acceptor.equals(StaticGameObject.class) && staticObjects == null)
				staticObjects = new ArrayList<StaticGameObject>();
			else if (acceptor.equals(DynamicGameObject.class) && dynamicObjects == null)
				dynamicObjects = new ArrayList<DynamicGameObject>();
			else if (acceptor.equals(GroundItem.class) && grounds == null)
				grounds = new ArrayList<GroundItem>();
		}
		return this;
	}

	/**
	 * Adds a entity to the repository.
	 * @param entity The entity to add.
	 * @return {@code true} if the entity was added, {@code false} if the
	 * entity has already been added.
	 */
	public boolean add(T entity) {
		final boolean add_entity = _add(entity);
		if (!add_entity)
			return false;
		switch (entity.type()) {
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
	 * Checks the acceptors.
	 * @param entity The entity to check against.
	 */
	public void checkAcceptors(T entity) {
		if (!acceptors.contains(entity.getClass()))
			throw new IllegalArgumentException("Illegal type of entity");
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
	 * Gets an entity from the repository.
	 * @param type The entity type.
	 * @return The entitys components.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Entity> List<E> get(int type) {
		switch (type) {
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
	 * Gets the entitys at the position.
	 * @param position The position.
	 * @param clazz The clazz of the entitys to search for.
	 * @return The list of the entitys.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Entity> List<E> get(Position position, Class<E> clazz) {
		final LinkedList<E> list = new LinkedList<E>();
		final List<T> entitys = this.entitys.get(position);
		if (entitys != null)
			for (final T entity : entitys)
				if (entity.getClass().equals(clazz))
					list.add((E) entity);
		return list;
	}

	/**
	 * Removes a entity from the repository.
	 * @param entity The entity to remove.
	 * @return {@code true} if the entity was removed, {@code false} if the
	 * entity was not found.
	 */
	public boolean remove(T entity) {
		final boolean remove_entity = _remove(entity);
		switch (entity.type()) {
		case Entity.PLAYER_TYPE:
			return players.remove(entity) & remove_entity;
		case Entity.NPC_TYPE:
			return npcs.remove(entity) & remove_entity;
		case Entity.STATIC_OBJECT_TYPE:
			return staticObjects.remove(entity) & remove_entity;
		case Entity.DYNAMIC_OBJECT_TYPE:
			return dynamicObjects.remove(entity) & remove_entity;
		case Entity.GROUND_TYPE:
			return grounds.remove(entity) & remove_entity;
		default:
			return remove_entity;
		}
	}

	@Override
	public String toString() {
		return EntityRepository.class.getName() + " [players=" + players + ", npcs=" + npcs + ", static_o=" + staticObjects + ", dynamic_o=" + dynamicObjects
				+ ", grounds=" + grounds + "]";
	}
}
