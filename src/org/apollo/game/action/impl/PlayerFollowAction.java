package org.apollo.game.action.impl;

import org.apollo.game.action.DistancedAction;
import org.apollo.game.model.Character;
import org.apollo.game.model.Position;

/**
 * An {@link DistancedAction} for when a player follows another player.
 * @author Steve
 */
public final class PlayerFollowAction extends DistancedAction<Character> {

	/**
	 * The acquaintance.
	 */
	private final Character acquaintance;

	/**
	 * Create a new player follow action.
	 * @param character The character.
	 * @param acquaintance The acquaintance.
	 */
	public PlayerFollowAction(Character character, Character acquaintance) {
		super(2, true, character, acquaintance.getPosition(), Position.MAX_DISTANCE);
		this.acquaintance = acquaintance;
	}

	@Override
	public void executeAction() {
		final Character player = getCharacter();
		final Character other = acquaintance;
		if (player.isDead() || !player.isActive() || other.isDead() || !other.isActive()) {
			stop();
			return;
		}
		if (player.getPosition().isWithinDistance(other.getLastPosition(), Position.MAX_DISTANCE)) {
			if (!player.getPosition().equals(other.getLastPosition())) {
				player.getWalkingQueue().walkTo(other.getLastPosition());
				player.startFacing(other.isControlling() ? 32768 + other.getIndex() : other.getIndex());
			}
		} else {
			player.sendMessage("You are too far away!");
			stop();
		}
	}

	/**
	 * Stops the action.
	 */
	@Override
	public void stop() {
		getCharacter().stopFacing();
		super.stop();
	}
}