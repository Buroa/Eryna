package org.apollo.game.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

import org.apollo.game.event.impl.BuildPlayerMenuEvent;
import org.apollo.game.event.impl.ConfigEvent;
import org.apollo.game.event.impl.UpdateRunEnergyEvent;
import org.apollo.game.pf.AStarPathFinder;
import org.apollo.game.pf.DumbPathFinder;
import org.apollo.game.pf.Path;
import org.apollo.game.pf.PathFinder;
import org.apollo.game.pf.TileMapBuilder;

/**
 * A queue of {@link Direction}s which a {@link Character} will follow.
 * @author Graham
 */
public final class WalkingQueue {

	/**
	 * Represents a single point in the queue.
	 * @author Graham
	 */
	private static final class Point {

		/**
		 * The point's position.
		 */
		private final Position position;

		/**
		 * The direction to walk to this point.
		 */
		private final Direction direction;

		/**
		 * Creates a point.
		 * @param position The position.
		 * @param direction The direction.
		 */
		public Point(Position position, Direction direction) {
			this.position = position;
			this.direction = direction;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return Point.class.getName() + " [direction=" + direction + ", position=" + position + "]";
		}
	}

	/**
	 * The maximum size of the queue. If any additional steps are added, they
	 * are discarded.
	 */
	private static final int MAXIMUM_SIZE = 128;

	/**
	 * The character whose walking queue this is.
	 */
	private final Character character;

	/**
	 * The queue of directions.
	 */
	private final Deque<Point> points = new ArrayDeque<Point>();

	/**
	 * The old queue of directions.
	 */
	private final Deque<Point> oldPoints = new ArrayDeque<Point>();

	/**
	 * Flag indicating if this queue (only) should be ran.
	 */
	private boolean runningQueue;

	/**
	 * Stop for this many pulses.
	 */
	private int stop;

	/**
	 * Flag indicating if we sent the attack menu.
	 */
	private boolean wildernessMenu = false;

	/**
	 * The last energy generation.
	 */
	private long lastEnergyGeneration = 0;

	/**
	 * Creates a walking queue for the specified character.
	 * @param character The character.
	 */
	public WalkingQueue(Character character) {
		this.character = character;
	}

	/**
	 * Adds the first step to the queue, attempting to connect the server and
	 * client position by looking at the previous
	 * queue.
	 * @param clientConnectionPosition The first step.
	 * @return {@code true} if the queues could be connected correctly,
	 * {@code false} if not.
	 */
	public boolean addFirstStep(Position clientConnectionPosition) {
		if (stop > 0)
			return false;
		final Position serverPosition = character.getPosition();
		int deltaX = clientConnectionPosition.getX() - serverPosition.getX();
		int deltaY = clientConnectionPosition.getY() - serverPosition.getY();
		if (Direction.isConnectable(deltaX, deltaY)) {
			points.clear();
			oldPoints.clear();
			addStep(clientConnectionPosition);
			return true;
		}
		final Queue<Position> travelBackQueue = new ArrayDeque<Position>();
		Point oldPoint;
		while ((oldPoint = oldPoints.pollLast()) != null) {
			final Position oldPosition = oldPoint.position;
			deltaX = oldPosition.getX() - serverPosition.getX();
			deltaY = oldPosition.getX() - serverPosition.getY();
			travelBackQueue.add(oldPosition);
			if (Direction.isConnectable(deltaX, deltaY)) {
				points.clear();
				oldPoints.clear();
				for (final Position travelBackPosition : travelBackQueue)
					addStep(travelBackPosition);
				addStep(clientConnectionPosition);
				return true;
			}
		}
		oldPoints.clear();
		return false;
	}

	/**
	 * Adds a step.
	 * @param x The x coordinate of this step.
	 * @param y The y coordinate of this step.
	 */
	private void addStep(int x, int y) {
		if (points.size() >= MAXIMUM_SIZE)
			return;
		final Point last = getLast();
		final int deltaX = x - last.position.getX();
		final int deltaY = y - last.position.getY();
		final Direction direction = Direction.fromDeltas(deltaX, deltaY);
		if (direction != Direction.NONE) {
			final Point p = new Point(new Position(x, y, last.position.getHeight()), direction);
			points.add(p);
			oldPoints.add(p);
		}
	}

	/**
	 * Adds a step to the queue.
	 * @param step The step to add.
	 */
	public void addStep(Position step) {
		if (stop > 0)
			return;
		final Point last = getLast();
		final int x = step.getX();
		final int y = step.getY();
		int deltaX = x - last.position.getX();
		int deltaY = y - last.position.getY();
		final int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0)
				deltaX++;
			else if (deltaX > 0)
				deltaX--;
			if (deltaY < 0)
				deltaY++;
			else if (deltaY > 0)
				deltaY--;
			addStep(x - deltaX, y - deltaY);
		}
	}

	/**
	 * Updates the player menu.
	 * @param position The position of the new click.
	 */
	private void characterMenuUpdate(Position position) {
		if (character.isControlling())
			if (position.isInWilderness() || ((Player) character).getSettings().isAttackable()) {
				if (!wildernessMenu) {
					character.send(new BuildPlayerMenuEvent(3, false, "Attack"));
					wildernessMenu = true;
				}
			} else if (wildernessMenu) {
				character.send(new BuildPlayerMenuEvent(3, false, "null"));
				wildernessMenu = false;
			}
	}

	/**
	 * Clears the walking queue.
	 */
	public void clear() {
		points.clear();
		oldPoints.clear();
	}

	/**
	 * Decreases this characters energy to run.
	 */
	private void decreaseEnergy() {
		if (character.isControlling()) {
			character.setRunEnergy(character.getRunEnergy() - 1);
			character.send(new UpdateRunEnergyEvent(character.getRunEnergy()));
			if (character.getRunEnergy() == 0) {
				character.send(new ConfigEvent(173, 0));
				runningQueue = false;
			}
		}
	}

	/**
	 * Finds the path.
	 * @param destination The destination.
	 * @return The path, or null if none found.
	 */
	public Path findPath(Position destination) {
		// Register the pathfinder
		PathFinder finder = new AStarPathFinder();
		if (!character.isControlling())
			finder = new DumbPathFinder();

		// Set the center position
		Position center = character.getPosition();
		if (points.peekLast() != null)
			center = points.peekLast().position;

		// Set the radius
		final int radius = center.getDistance(destination);

		// check if radius is too big
		if (radius > 64 || radius <= 0)
			return null;

		// The destination in the map
		final int x = destination.getX() - center.getX();
		final int y = destination.getY() - center.getY();

		// Build the tiles
		final TileMapBuilder builder = new TileMapBuilder(center, radius);
		builder.build();

		// Find the path
		return finder.findPath(center, radius, builder.build(), radius, radius, x + radius, y + radius);
	}

	/**
	 * Generates the characters energy.
	 */
	private void generateEnergy() {
		if (character.isControlling()) {
			final int energy = character.getRunEnergy();
			if (energy < 100) {
				final int formula = 2260 - character.getSkillSet().getSkill(Skill.AGILITY).getCurrentLevel() * 10;
				if (System.currentTimeMillis() > formula + lastEnergyGeneration) {
					lastEnergyGeneration = System.currentTimeMillis();
					character.setRunEnergy(energy + 1);
					character.send(new UpdateRunEnergyEvent(character.getRunEnergy()));
				}
			}
		}
	}

	/**
	 * Gets the last point.
	 * @return The last point.
	 */
	private Point getLast() {
		final Point last = points.peekLast();
		if (last == null)
			return new Point(character.getPosition(), Direction.NONE);
		return last;
	}

	/**
	 * Gets the running queue flag.
	 * @return True if running queue, false if not.
	 */
	public boolean getRunningQueue() {
		return runningQueue;
	}

	/**
	 * Called every pulse, updates the queue.
	 */
	public void pulse() {
		if (stop > 0)
			stop--;

		Position position = character.getPosition();
		Direction first = Direction.NONE;
		Direction second = Direction.NONE;

		Point next = points.poll();
		if (next != null) {
			character.setLastPosition(position);
			first = next.direction;
			position = next.position;
			if (runningQueue && character.getRunEnergy() > 0) {
				next = points.poll();
				if (next != null) {
					second = next.direction;
					position = next.position;
					decreaseEnergy();
				}
			}
			characterMenuUpdate(position);
		} else
			generateEnergy();

		character.setDirections(first, second);
		boolean update = false;
		if (first != Direction.NONE) {
			character.setMoved(1);
			update = true;
		}
		if (second != Direction.NONE) {
			character.setMoved(2);
			update = true;
		}
		if (update)
			character.setPosition(position);
	}

	/**
	 * Sets the running queue flag.
	 * @param runningQueue The running queue flag.
	 */
	public void setRunningQueue(boolean runningQueue) {
		this.runningQueue = runningQueue;
	}

	/**
	 * Gets the size of the queue.
	 * @return The size of the queue.
	 */
	public int size() {
		return points.size();
	}

	/**
	 * Disables the movement queue.
	 * @param stop The time to stop the movement queue.
	 */
	public void stop(int stop) {
		this.stop = stop;
	}

	/**
	 * Walks to the position.
	 * @param position The position to walk too.
	 */
	public void walkTo(Position position) {
		if (position == null || stop > 0)
			return;
		final Path path = findPath(position);
		if (path != null) {
			int i = 0;
			for (final org.apollo.game.pf.Point point : path.getPoints()) {
				final Position step = new Position(point.getX(), point.getY(), character.getPosition().getHeight());
				if (i++ == 1 && points.peekLast() == null) {
					if (!addFirstStep(step))
						return;
				} else
					addStep(step);
			}
		}
	}
}
