package org.apollo.game.scheduling.impl;

import org.apollo.game.model.World;
import org.apollo.game.model.inter.store.Shop;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * An {@link ScheduledTask} which processes the items in the shops.
 * @author Steve
 */
public final class ShopsScheduledTask extends ScheduledTask {

	/**
	 * Initializes the shop task.
	 */
	public ShopsScheduledTask() {
		super(10, true);
	}

	@Override
	public void execute() {
		for (final Shop shop : World.getWorld().getStores().getShops().values())
			shop.process();
	}

}
