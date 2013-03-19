package org.apollo.game.sync;

import org.apollo.game.GameService;
import org.apollo.game.model.Npc;
import org.apollo.game.model.World;
import org.apollo.game.sync.task.NpcSynchronizationTask;
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
 * @author Steve
 */
public final class SequentialNpcSynchronizer extends ClientSynchronizer {

	@Override
	public void synchronize() {
		final CharacterRepository<Npc> npcs = World.getWorld().getNpcRepository();

		for (final Npc npc : npcs) {
			final SynchronizationTask task = new NpcSynchronizationTask(npc);
			task.run();
		}
	}
}
