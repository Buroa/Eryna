package org.apollo.game.scheduling.impl;

import org.apollo.game.model.GroundItem;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * An {@link ScheduledTask} that processes ground item ticks.
 * @author Steve
 */
public final class ProcessGroundItemsTask extends ScheduledTask {

	/**
	 * Creates the process ground items task.
	 */
	public ProcessGroundItemsTask() {
		super(1, true);
	}

	@Override
	public void execute() {
		for (final GroundItem item : World.getWorld().getItems()) {
			if (item.getPulses() != 0)
				item.decreasePulses();
			if (item.getDeletes() != 0)
				item.decreaseDeletes();
			else
				World.getWorld().unregister(item);
		}
	}

}
