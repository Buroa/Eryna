package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PlayerIdleEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;

/**
 * An {@link EventHandler} for the {@link PlayerIdleEvent}
 * @author Steve
 */
public final class PlayerIdleEventHandler extends EventHandler<PlayerIdleEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, PlayerIdleEvent event) {
		if (player.getPrivilegeLevel().equals(PrivilegeLevel.DEVELOPER)) {
			ctx.breakHandlerChain();
			return;
		}
		player.logout();
	}
}