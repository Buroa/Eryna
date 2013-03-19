package org.apollo.game.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadFactory;

import org.apollo.game.GameService;
import org.apollo.game.model.Npc;
import org.apollo.game.model.World;
import org.apollo.game.sync.task.NpcSynchronizationTask;
import org.apollo.game.sync.task.PhasedSynchronizationTask;
import org.apollo.game.sync.task.SynchronizationTask;
import org.apollo.util.CharacterRepository;
import org.apollo.util.NamedThreadFactory;

/**
 * An implementation of {@link ClientSynchronizer} which runs in a thread pool.
 * A {@link Phaser} is used to ensure that
 * the synchronization is complete, allowing control to return to the
 * {@link GameService} that started the
 * synchronization. This class will scale well with machines that have multiple
 * cores/processors. The {@link SequentialClientSynchronizer} will work better
 * on machines with a single core/processor, however, both classes
 * will work.
 * @author Graham
 */
public final class ParallelNpcSynchronizer extends ClientSynchronizer {

	/**
	 * The executor service.
	 */
	private final ExecutorService executor;

	/**
	 * The phaser.
	 */
	private final Phaser phaser = new Phaser(1);

	/**
	 * Creates the parallel npc synchronizer backed by a thread pool with a
	 * number of threads equal to the number of
	 * processing cores available (this is found by the.
	 * {@link Runtime#availableProcessors()} method.
	 */
	public ParallelNpcSynchronizer() {
		final int processors = Runtime.getRuntime().availableProcessors();
		final ThreadFactory factory = new NamedThreadFactory("NpcSynchronizer");
		executor = Executors.newFixedThreadPool(processors, factory);
	}

	@Override
	public void synchronize() {
		final CharacterRepository<Npc> npcs = World.getWorld().getNpcRepository();
		final int npcCount = npcs.size();

		phaser.bulkRegister(npcCount);
		for (final Npc npc : npcs) {
			final SynchronizationTask task = new NpcSynchronizationTask(npc);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();
	}
}
