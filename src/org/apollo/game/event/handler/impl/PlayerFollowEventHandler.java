package org.apollo.game.event.handler.impl;

import org.apollo.game.action.impl.PlayerFollowAction;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PlayerOptionEvent;
import org.apollo.game.model.Player;

/**
 * An {@link EventHandler} for when a player follows another player.
 * @author Steve
 */
public final class PlayerFollowEventHandler extends EventHandler<PlayerOptionEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, PlayerOptionEvent event) {
		if (event.getOption() == 2) {
			player.startAction(new PlayerFollowAction(player, event.getPlayer()));
			ctx.breakHandlerChain();
		}
	}
}