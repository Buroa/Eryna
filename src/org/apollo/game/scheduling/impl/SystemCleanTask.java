package org.apollo.game.scheduling.impl;

import org.apollo.game.scheduling.ScheduledTask;

/**
 * An {@link ScheduledTask} that runs system functions to regain lost memory.
 * @author Steve
 */
public final class SystemCleanTask extends ScheduledTask {

	/**
	 * Creates a system clean task.
	 */
	public SystemCleanTask() {
		super(5 * 60, true);
	}

	@Override
	public void execute() {
		System.gc();
		System.runFinalization();
	}

}
