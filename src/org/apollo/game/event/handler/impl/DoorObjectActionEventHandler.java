package org.apollo.game.event.handler.impl;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.action.DistancedAction;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ObjectActionEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.obj.DynamicGameObject;
import org.apollo.game.model.obj.GameObject;
import org.apollo.game.model.region.Chunk;

/**
 * An {@link EventHandler} for the {@link ObjectActionEvent}
 * @author thispixel
 * @author Steve
 */
public final class DoorObjectActionEventHandler extends EventHandler<ObjectActionEvent> {

	/**
	 * An {@link DistancedAction}
	 * @author Steve
	 */
	private final class DoorAction extends DistancedAction<Player> {

		/**
		 * The player.
		 */
		private final Player player;

		/**
		 * The door object.
		 */
		private final DoorObject doorObject;

		/**
		 * The door id.
		 */
		private final int id;

		/**
		 * Creates a new door action.
		 * @param player The player.
		 * @param doorObject The door object.
		 * @param id The door id.
		 */
		private DoorAction(Player player, DoorObject doorObject, int id) {
			super(0, true, player, doorObject.getGameObject().getPosition(), 1);
			this.player = player;
			this.doorObject = doorObject;
			this.id = id;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (obj.getClass() != getClass())
				return false;
			final DoorAction other = (DoorAction) obj;
			if (doorObject != other.doorObject)
				return false;
			if (id != other.id)
				return false;
			return true;
		}

		@Override
		public void executeAction() {
			final GameObject gameObject = doorObject.getGameObject();
			final Position position = gameObject.getPosition();
			final int rotation = doorObject.isOpen() ? gameObject.getRotation() : gameObject.getRotation() + 1;
			player.turnTo(position);
			final Chunk chunk = World.getWorld().getRegionManager().getChunkByPosition(position);
			final DynamicGameObject object = new DynamicGameObject(id, position, gameObject.getType(), rotation, true, true);
			chunk.add(object, 200); // 2 minute delay to close them
			doorObject.setOpen(!doorObject.isOpen());
			stop();
		}
	}

	/**
	 * Door object which holds the map object and the status of the door.
	 * @author Steve
	 */
	private static final class DoorObject {

		/**
		 * The game object.
		 */
		private final GameObject gameObject;

		/**
		 * The open flag.
		 */
		private boolean isOpen = false;

		/**
		 * Create a new door object.
		 * @param gameObject The game object.
		 */
		public DoorObject(GameObject gameObject) {
			this.gameObject = gameObject;
		}

		/**
		 * Gets the game object.
		 * @return The game object.
		 */
		public GameObject getGameObject() {
			return gameObject;
		}

		/**
		 * Get if this door is open or closed.
		 * @return True if door is open, false if otherwise.
		 */
		public boolean isOpen() {
			return isOpen;
		}

		/**
		 * Set the door open or closed
		 * @param isOpen True if door is open, false if otherwise.
		 */
		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}
	}

	/**
	 * Map with the Location being mapped to the door, only doors which have
	 * been opened / closed to save a little bit of memory
	 */
	private static Map<Position, DoorObject> doors = new HashMap<Position, DoorObject>();

	/**
	 * Id's of each door object
	 */
	private static final int[] DOORS = { 6749, 6730, 6727, 6748, 6624, 6729,
			6625, 6725, 6726, 4428, 6745, 6743, 4467, 4423, 4424, 4465, 1512,
			6724, 1516, 1519, 1530, 1531, 1533, 1534, 11712, 11711, 11708,
			1536, 6725, 3198, 3197, 11707, 4312, 2266 };

	/**
	 * Is the given id an door?
	 * @param id The id to check
	 * @return True if the object is a door, false otherwise.
	 */
	private static boolean isDoor(int id) {
		for (final int door : DOORS)
			if (id == door)
				return true;
		return false;
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ObjectActionEvent event) {
		if (isDoor(event.getId())) {
			DoorObject door = doors.get(event.getPosition());
			if (door == null) {
				final GameObject gameObject =
						World.getWorld().getRegionManager().getRegionByPosition
								(event.getPosition()).getObject(event.getPosition(), 0);
				if (gameObject != null) {
					door = new DoorObject(gameObject);
					doors.put(event.getPosition(), door);
				} else
					return;
			}
			player.startAction(new DoorAction(player, door, event.getId()));
			ctx.breakHandlerChain();
		}

	}

}