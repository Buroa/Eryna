package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ButtonEvent;
import org.apollo.game.model.Player;

/**
 * An {@link EventHandler} which intercepts button clicks on interfaces, and
 * forwards the event to the current listener.
 * @author Chris Fletcher
 */
public final class InterfaceButtonEventHandler extends EventHandler<ButtonEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ButtonEvent event) {
		final boolean shouldBreak = player.getInterfaceSet().buttonClicked(event.getInterfaceId());

		if (shouldBreak)
			ctx.breakHandlerChain();
	}
}