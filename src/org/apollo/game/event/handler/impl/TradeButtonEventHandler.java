package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ButtonEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.trade.TradeSession;

/**
 * An {@link EventHandler} which responds to {@link ButtonEvent}s for trade
 * requests.
 * @author Steve
 */
public final class TradeButtonEventHandler extends EventHandler<ButtonEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ButtonEvent event) {
		final int id = event.getInterfaceId();
		if (id == 3420 || id == 3546) {
			final TradeSession session = player.getSettings().getTradeSession();
			if (session != null)
				session.accept();
			ctx.breakHandlerChain();
		}
	}
}
