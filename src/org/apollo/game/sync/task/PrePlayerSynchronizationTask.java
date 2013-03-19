package org.apollo.game.sync.task;

import org.apollo.game.event.impl.RegionChangeEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;

/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the
 * specified {@link Player}.
 * @author Graham
 */
public final class PrePlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PrePlayerSynchronizationTask} for the specified
	 * player.
	 * @param player The player.
	 */
	public PrePlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	/**
	 * Checks if a region update is required.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	private boolean isRegionUpdateRequired() {
		final Position current = player.getPosition();
		final Position last = player.getLastKnownRegion();
		final int deltaX = current.getLocalX(last);
		final int deltaY = current.getLocalY(last);
		return deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY >= 88;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		player.getWalkingQueue().pulse();

		if (player.isTeleporting())
			player.resetViewingDistance();
		if (!player.hasLastKnownRegion() || isRegionUpdateRequired()) {
			final Position position = player.getPosition();
			player.setRegionChanged(true);
			player.setLastKnownRegion(position);
			player.send(new RegionChangeEvent(position));
		}
	}
}
