package org.apollo.game.sync.task;

import org.apollo.game.model.Npc;
import org.apollo.game.model.Position;
import org.apollo.util.TextUtil;

/**
 * A {@link SynchronizationTask} which does synchronization work for the
 * specified {@link Npc}.
 * @author Steve
 */
public final class NpcSynchronizationTask extends SynchronizationTask {

	/**
	 * The npc.
	 */
	private final Npc npc;

	/**
	 * Creates the {@link NpcSynchronizationTask} for the specified npc.
	 * @param npc The npc.
	 */
	public NpcSynchronizationTask(Npc npc) {
		this.npc = npc;
	}

	/**
	 * Random walks the npc.
	 * @param npc The npc to walk randomly.
	 */
	private void randomWalk(Npc npc) {
		if (!npc.isRandomWalking())
			return;
		if (!npc.isRandomWalkingExec())
			return;
		if (TextUtil.random(4) == 1) {
			int MoveX = TextUtil.random(1);
			int MoveY = TextUtil.random(1);
			final int Rnd = TextUtil.random(4);
			if (Rnd == 1) {
				MoveX = -MoveX;
				MoveY = -MoveY;
			} else if (Rnd == 2)
				MoveX = -MoveX;
			else if (Rnd == 3)
				MoveY = -MoveY;
			final Position walkto = npc.getPosition().transform(MoveX, MoveY, 0);
			if (walkto.isWithinDistance(npc.getFirstPosition(), 6)) {
				npc.getWalkingQueue().walkTo(walkto);
				npc.setRandomWalkingExec(false);
			}
		}
	}

	@Override
	public void run() {
		npc.decrementMoving();
		randomWalk(npc);
		npc.getWalkingQueue().pulse();
		npc.setTeleporting(false);
		npc.resetBlockSet();
		npc.pulse();
	}
}
