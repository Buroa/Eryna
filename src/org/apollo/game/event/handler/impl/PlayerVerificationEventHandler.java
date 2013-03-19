package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PlayerOptionEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;
import org.apollo.game.model.World;

/**
 * An {@link EventHandler} for the {@link PlayerOptionEvent}
 * @author Steve
 */
public final class PlayerVerificationEventHandler extends EventHandler<PlayerOptionEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, PlayerOptionEvent event) {
		final int id = event.getPlayerId();
		if (id < 0)
			ctx.breakHandlerChain();
		else if (World.getWorld().getPlayerRepository().forIndex(id) == null)
			ctx.breakHandlerChain();
		else if (event.getPlayer().getPrivilegeLevel().equals(PrivilegeLevel.DEVELOPER))
			if (event.getOption() == 1) {
				player.sendMessage("Sorry, but Developers are protected from your attacks.");
				ctx.breakHandlerChain();
			}
			else if (event.getOption() == 3) {
				player.sendMessage("Sorry, but you can't trade with Konklex Developers.");
				ctx.breakHandlerChain();
			}
	}
}
