package org.apollo.game.action;

import org.apollo.game.model.Character;
import org.apollo.game.model.Position;

/**
 * An {@link Action} which fires when a distance requirement is met.
 * @param <T> The character
 * @author Steve
 * @author Graham
 * @author Blake
 */
public abstract class MovingDistancedAction<T extends Character> extends Action<T> {

	/**
	 * The position to distance check with.
	 */
	private final Position position;

	/**
	 * The minimum distance before the action fires.
	 */
	private final int distance;

	/**
	 * The delay once the threshold is reached.
	 */
	private final int delay;

	/**
	 * A flag indicating if this action fires immediately after the threshold is
	 * reached.
	 */
	private final boolean immediate;

	/**
	 * A flag indicating if the distance has been reached yet.
	 */
	private boolean reached = false;

	/**
	 * Creates a new DistancedAction.
	 * @param delay The delay between executions once the distance threshold is
	 * reached.
	 * @param immediate Whether or not this action fires immediately after the
	 * distance threshold is reached.
	 * @param character The character.
	 * @param position The position.
	 * @param distance The distance.
	 */
	public MovingDistancedAction(int delay, boolean immediate, T character, Position position, int distance) {
		super(0, true, character);
		this.position = position;
		this.distance = distance;
		this.delay = delay;
		this.immediate = immediate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.game.scheduling.ScheduledTask#execute()
	 */
	@Override
	public void execute() {
		if (getCharacter().getPosition().getDistance(position) <= distance) {
			reached = true;
			setDelay(delay);
			if (immediate) {
				executeAction();
				return;
			}
		} else {
			if (getCharacter().getWalkingQueue().size() == 0) {
				final int first_distance = getMove(getCharacter().getPosition().getX(), position.getX());
				final int second_distance = getMove(getCharacter().getPosition().getY(), position.getY());
				getCharacter().getWalkingQueue().walkTo(position.transform(first_distance, second_distance, 0));
			}
			reached = false;
			setDelay(0);
		}
		if (reached)
			executeAction();
	}

	/**
	 * Executes the actual action. Called when the distance requirement is met.
	 */
	public abstract void executeAction();

	/**
	 * Gets the distance between the specified distances.
	 * @param i The first distance.
	 * @param j The second distance.
	 * @return The distance between the specified distances.
	 */
	private int getMove(int i, int j) {
		if (i - j == 0)
			return 0;
		if (i - j < 0)
			return 1;
		return i - j <= 0 ? 0 : -1;
	}
}
