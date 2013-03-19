package org.apollo.game.scheduling.impl;

import org.apollo.game.model.Player;
import org.apollo.game.model.skill.farming.FarmingSet;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * An {@link ScheduledTask} that updates farming configurations.
 * @author Steve
 */
public final class FarmingScheduledTask extends ScheduledTask {

	/**
	 * The player this task is scheduled too.
	 */
	private final Player player;

	/**
	 * Schedules a new task for the specified player.
	 * @param player The player to register the schedule too.
	 */
	public FarmingScheduledTask(Player player) {
		super(25, true);
		this.player = player;
	}

	@Override
	public void execute() {
		if (!player.isActive() || !player.getSettings().isFarming())
			stop();
		else {
			final FarmingSet farmingSet = player.getSettings().getFarmingSet();
			farmingSet.getAllotment().doCalculations();
			farmingSet.getBushes().doCalculations();
			farmingSet.getFlowers().doCalculations();
			farmingSet.getHerbs().doCalculations();
			farmingSet.getHops().doCalculations();
			farmingSet.getSpecialPlantOne().doCalculations();
			farmingSet.getSpecialPlantTwo().doCalculations();
			farmingSet.getFruitTrees().doCalculations();
		}
	}
}