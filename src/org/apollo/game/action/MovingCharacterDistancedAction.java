package org.apollo.game.action;

import org.apollo.game.model.Character;

/**
 * An {@link Action} which fires when a distance requirement is met.
 * @param <T> The character
 * @author Steve
 * @author Graham
 * @author Blake
 */
public abstract class MovingCharacterDistancedAction<T extends Character> extends Action<T> {

	/**
	 * The other character.
	 */
	private final T other;

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
	 * @param other The other character.
	 * @param distance The distance.
	 */
	public MovingCharacterDistancedAction(int delay, boolean immediate, T character, T other, int distance) {
		super(0, true, character);
		this.other = other;
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
		if (reached) {
			if (getCharacter().getPosition().getDistance(other.getPosition()) > distance)
				reached = false;
			else
				executeAction();
		}
		else if (getCharacter().getPosition().getDistance(other.getPosition()) <= distance) {
			reached = true;
			setDelay(delay);
			if (immediate)
				executeAction();
		} else if (getCharacter().getWalkingQueue().size() == 0)
			getCharacter().getWalkingQueue().walkTo(other.getLastPosition());
	}

	/**
	 * Executes the actual action. Called when the distance requirement is met.
	 */
	public abstract void executeAction();
}
