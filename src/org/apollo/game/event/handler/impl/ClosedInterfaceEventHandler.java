package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ClosedInterfaceEvent;
import org.apollo.game.model.Player;

/**
 * An {@link EventHandler} for the {@link ClosedInterfaceEvent}.
 * @author Graham
 */
public final class ClosedInterfaceEventHandler extends EventHandler<ClosedInterfaceEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, ClosedInterfaceEvent event) {
		player.getInterfaceSet().interfaceClosed();
	}
}
