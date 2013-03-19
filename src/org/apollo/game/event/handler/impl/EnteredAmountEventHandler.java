package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.EnteredAmountEvent;
import org.apollo.game.model.Player;

/**
 * An {@link EventHandler} for the {@link EnteredAmountEvent}.
 * @author Graham
 */
public final class EnteredAmountEventHandler extends EventHandler<EnteredAmountEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, EnteredAmountEvent event) {
		player.getInterfaceSet().enteredAmount(event.getAmount());
	}
}
