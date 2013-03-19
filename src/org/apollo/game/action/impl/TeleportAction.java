package org.apollo.game.action.impl;

import org.apollo.game.action.Action;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Character;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Position;

/**
 * An {@link Action} that teleports the player.
 * @author Steve
 */
public final class TeleportAction extends Action<Character> {

	/**
	 * The pulses.
	 */
	private int pulses = 0;

	/**
	 * The teleporting position.
	 */
	private final Position position;

	/**
	 * Create the teleport action on the specified player.
	 * @param player The player that will execute this action.
	 * @param position The new position the player will be teleported too.
	 */
	public TeleportAction(Character player, Position position) {
		super(0, true, player);
		this.position = position;
	}

	@Override
	public boolean equals(Object other) {
		return getClass() == other.getClass();
	}

	@Override
	public void execute() {
		final Character player = getCharacter();
		if (pulses == 0)
			player.playAnimation(new Animation(714));
		else if (pulses == 1) {
			player.playGraphic(new Graphic(308, 15, 100));
			setDelay(1);
		} else if (pulses == 2) {
			player.stopGraphic();
			player.playAnimation(new Animation(715));
			player.teleport(position);
			super.stop();
		}
		pulses++;
	}

	@Override
	public void stop() {
		// do nothing
	}
}
