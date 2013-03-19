package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.WalkEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;
import org.apollo.game.model.Position;
import org.apollo.game.model.WalkingQueue;

/**
 * A handler for the {@link WalkEvent}.
 * @author Graham
 */
public final class WalkEventHandler extends EventHandler<WalkEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, WalkEvent event) {
		final WalkingQueue queue = player.getWalkingQueue();
		final Position[] steps = event.getSteps();
		final Position destination = steps[steps.length - 1];
		if (player.getPrivilegeLevel().equals(PrivilegeLevel.DEVELOPER) && event.isRunning()) {
			final Position position = destination.transform(0, 0, player.getPosition().getHeight());
			player.teleport(position);
		} else
			player.getWalkingQueue().walkTo(destination);
		if (queue.size() > 0) {
			player.stopAction();
			player.getInterfaceSet().close();
		}
	}
}
