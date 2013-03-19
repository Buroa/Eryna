package org.apollo.game.sync;

import org.apollo.game.GameService;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.game.sync.task.PlayerSynchronizationTask;
import org.apollo.game.sync.task.PostPlayerSynchronizationTask;
import org.apollo.game.sync.task.PrePlayerSynchronizationTask;
import org.apollo.game.sync.task.SynchronizationTask;
import org.apollo.util.CharacterRepository;

/**
 * An implementation of {@link ClientSynchronizer} which runs in a single thread
 * (the {@link GameService} thread from
 * which this is called). Each client is processed sequentially. Therefore this
 * class will work well on machines with a
 * single core/processor. The {@link ParallelClientSynchronizer} will work
 * better on machines with multiple
 * cores/processors, however, both classes will work.
 * @author Graham
 */
public final class SequentialClientSynchronizer extends ClientSynchronizer {

	@Override
	public void synchronize() {
		final CharacterRepository<Player> players = World.getWorld().getPlayerRepository();
		for (final Player player : players) {
			final SynchronizationTask task = new PrePlayerSynchronizationTask(player);
			task.run();
		}

		for (final Player player : players) {
			final SynchronizationTask task = new PlayerSynchronizationTask(player);
			task.run();
		}

		for (final Player player : players) {
			final SynchronizationTask task = new PostPlayerSynchronizationTask(player);
			task.run();
		}
	}
}
