package org.apollo.game.sync.task;

import org.apollo.game.model.Player;
import org.apollo.game.model.melee.Prayer;

/**
 * A {@link SynchronizationTask} which does post-synchronization work for the
 * specified {@link Player}.
 * @author Graham
 */
public final class PostPlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PostPlayerSynchronizationTask} for the specified
	 * player.
	 * @param player The player.
	 */
	public PostPlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		player.decrementMoving();
		player.setTeleporting(false);
		player.setRegionChanged(false);
		player.resetBlockSet();
		if (!player.isExcessivePlayersSet())
			player.incrementViewingDistance();
		else {
			player.decrementViewingDistance();
			player.resetExcessivePlayers();
		}

		/* Others */
		/* TODO Move */
		Prayer.drainPrayer(player);
	}
}
