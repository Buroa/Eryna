package org.apollo.game.scheduling.impl;

import org.apollo.game.model.Npc;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.TextUtil;

/**
 * An {@link ScheduledTask} that randomizes npc movements.
 * @author Steve
 */
public final class RandomNpcMovementScheduledTask extends ScheduledTask {

	/**
	 * Initialzes this class.
	 */
	public RandomNpcMovementScheduledTask() {
		super(15, true);
	}

	@Override
	public void execute() {
		for (final Npc npc : World.getWorld().getNpcRepository()) {
			if (npc.isRandomWalking())
				if (npc.getRegion().getPlayers().size() == 0)
					continue;
			npc.setRandomWalkingExec(true);
		}

		// Set a random execution delay
		setDelay(TextUtil.random(25));
	}

}
