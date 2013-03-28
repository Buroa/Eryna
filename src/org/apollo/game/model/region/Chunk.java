package org.apollo.game.model.region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.game.event.impl.CreateGroundEvent;
import org.apollo.game.event.impl.CreateObjectEvent;
import org.apollo.game.event.impl.DestroyGroundEvent;
import org.apollo.game.event.impl.DestroyObjectEvent;
import org.apollo.game.event.impl.RegionUpdateEvent;
import org.apollo.game.model.Entity;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.obj.DynamicGameObject;
import org.apollo.game.model.obj.GameObject;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.EntityRepository;

/**
 * A chunk which constists of 8x8 tiles.
 * @author Steve
 */
public final class Chunk {

	/**
	 * An {@link ScheduledTask} which sends out updates every pulse.
	 * @author Steve
	 */
	private final class ChunkScheduledTask extends ScheduledTask {

		/**
		 * The chunk.
		 */
		private final Chunk chunk;

		/**
		 * Creates the chunk scheduled task.
		 * @param chunk The chunk.
		 */
		public ChunkScheduledTask(Chunk chunk) {
			super(1, true);
			this.chunk = chunk;
		}

		@Override
		public void execute() {
			chunk.getRegion(chunk.getChunkPosition()).update(chunk);
			chunk.setTask(null);
			stop();
		}

	}

	/**
	 * The chunk size.
	 */
	public static final int SIZE = 8;

	/**
	 * The region position.
	 */
	private final Position position;

	/**
	 * The temporary events in this chunk.
	 */
	private final List<Event> events = new LinkedList<Event>();

	/**
	 * The entity repository.
	 */
	private final EntityRepository<Entity> repository = new EntityRepository<Entity>();

	/**
	 * The chunk absolute position.
	 */
	private final Position chunkPosition;

	/**
	 * The chunk scheduled task.
	 */
	private ChunkScheduledTask task;

	/**
	 * Creates a new chunk.
	 * @param position The chunk position.
	 */
	public Chunk(Position position) {
		this.position = position;
		this.chunkPosition = new Position(position.getX() << 3, position.getY() << 3);
	}

	/**
	 * Adds a timed dynamic game object.
	 * @param object The dynamic game object.
	 * @param time The time in pulses.
	 */
	public void add(final DynamicGameObject object, int time) {
		add(object);
		World.getWorld().schedule(new ScheduledTask(time, false) {

			@Override
			public void execute() {
				remove(object);
				if (!object.isDeleting())
					add(new DestroyObjectEvent(object), 1);
				if (object.isReplacing()) {
					final GameObject old = getRegion(object.getPosition()).getObject(object.getPosition(), object.getType());
					if (old != null)
						add(new CreateObjectEvent(old), 1);
				}
				stop();
			}
		});
	}

	/**
	 * Adds an entity.
	 * @param entity The entity.
	 */
	public void add(Entity entity) {
		repository.add(entity);
		// TODO make this neater
		switch (entity.type()) {
		case Entity.DYNAMIC_OBJECT_TYPE:
		case Entity.GROUND_TYPE:
			schedule();
			break;
		}
	}

	/**
	 * Adds a event.
	 * @param event The event.
	 */
	public void add(Event event) {
		events.add(event);
		schedule();
	}

	/**
	 * Adds a timed event.
	 * @param event The event.
	 * @param time The time.
	 */
	public void add(final Event event, int time) {
		add(event);
		World.getWorld().schedule(new ScheduledTask(time, false) {

			@Override
			public void execute() {
				remove(event);
				stop();
			}
		});
	}

	/**
	 * Adds a timed ground item.
	 * @param item The ground item.
	 * @param time The time in pulses to delete the item.
	 */
	public void add(final GroundItem item, int time) {
		add(item);
		World.getWorld().schedule(new ScheduledTask(time, false) {

			@Override
			public void execute() {
				remove(item);
				add(new DestroyGroundEvent(item), 1); // 0?
				stop();
			}
		});
	}

	/**
	 * Gets the chunk position.
	 * @return The chunk position.
	 */
	public Position getChunkPosition() {
		return chunkPosition;
	}

	/**
	 * Gets the events in this chunk.
	 * @return The events in this chunk.
	 */
	public final Collection<Event> getEvents() {
		return Collections.unmodifiableCollection(new LinkedList<Event>(events));
	}

	/**
	 * Gets the items in this chunk.
	 * @return The items in this chunk.
	 */
	public final Collection<GroundItem> getItems() {
		final List<GroundItem> list = repository.get(GroundItem.class);
		return Collections.unmodifiableCollection(new LinkedList<GroundItem>(list));
	}

	/**
	 * Gets the objects in this chunk.
	 * @param position The position.
	 * @return The objects in this chunk.
	 */
	public final Collection<DynamicGameObject> getObject(Position position) {
		final List<DynamicGameObject> list = repository.get(position, DynamicGameObject.class);
		return Collections.unmodifiableCollection(new LinkedList<DynamicGameObject>(list));
	}

	/**
	 * Gets the objects in this chunk.
	 * @return The objects in this chunk.
	 */
	public final Collection<DynamicGameObject> getObjects() {
		final List<DynamicGameObject> list = repository.get(DynamicGameObject.class);
		return Collections.unmodifiableCollection(new LinkedList<DynamicGameObject>(list));
	}

	/**
	 * Gets the region position.
	 * @return The region position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the region.
	 * @param position The position.
	 * @return The region.
	 */
	protected Region getRegion(Position position) {
		final RegionManager regionManager = World.getWorld().getRegionManager();
		return regionManager.getRegionByPosition(position);
	}

	/**
	 * Makes the chunk event.
	 * @param player The player.
	 * @return The region update event for the chunk.
	 */
	public RegionUpdateEvent make(Player player) {
		final Position regionPosition = player.getLastKnownRegion() != null ? player.getLastKnownRegion() : player.getPosition();
		final Position chunkPosition = getChunkPosition();
		final List<Event> events = new ArrayList<Event>();
		events.addAll(this.events);
		for (final DynamicGameObject object : getObjects()) {
			if (object.isDeleting()) {
				events.add(new DestroyObjectEvent(object));
				if (!object.isReplacing())
					continue;
			}
			events.add(new CreateObjectEvent(object));
		}
		for (final GroundItem item : getItems())
			if (item.continued(player.getName()))
				events.add(new CreateGroundEvent(item));
		events.removeAll(player.getLocalEventList());
		return new RegionUpdateEvent(regionPosition, chunkPosition, events);
	}

	/**
	 * Removes an entity.
	 * @param entity The entity.
	 */
	public void remove(Entity entity) {
		repository.remove(entity);
		// TODO should this be neater
		switch (entity.type()) {
		case Entity.DYNAMIC_OBJECT_TYPE:
			add(new DestroyObjectEvent((DynamicGameObject) entity), 1);
			break;
		case Entity.GROUND_TYPE:
			add(new DestroyGroundEvent((GroundItem) entity), 1);
			break;
		}
	}

	/**
	 * Removes a event.
	 * @param event The event.
	 */
	public void remove(Event event) {
		events.remove(event);
	}

	/**
	 * Schedules a chunk update.
	 */
	private void schedule() {
		if (task == null) {
			final ChunkScheduledTask task = new ChunkScheduledTask(this);
			this.task = task;
			World.getWorld().schedule(task);
		}
	}

	/**
	 * Sets the chunk scheduled task.
	 * @param task The task.
	 */
	protected void setTask(ChunkScheduledTask task) {
		this.task = task;
	}

	@Override
	public String toString() {
		return Chunk.class.getName() + " [pos=" + position + ", chunk=" + getChunkPosition() + ", events=" + events + "]";
	}

}
