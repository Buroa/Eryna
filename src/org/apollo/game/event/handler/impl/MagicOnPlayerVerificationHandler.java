package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.MagicEvent;
import org.apollo.game.event.impl.MagicOnPlayerEvent;
import org.apollo.game.model.Npc;
import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;

/**
 * An {@link EventHandler} for the {@link MagicOnPlayerEvent}
 * @author Steve
 */
public final class MagicOnPlayerVerificationHandler extends EventHandler<MagicEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, MagicEvent event) {
		if (event.getVictim() == null) {
			ctx.breakHandlerChain();
			return;
		}
		switch (event.getOption()) {
		case 0:
			final Player clicked = (Player) event.getVictim();
			if (clicked.getPrivilegeLevel().equals(PrivilegeLevel.DEVELOPER)) {
				player.sendMessage("Sorry, but Developers are protected from your attacks.");
				ctx.breakHandlerChain();
			}
			break;
		case 1:
			final Npc npc = (Npc) event.getVictim();
			if (!npc.getDefinition().isAttackable())
				ctx.breakHandlerChain();
			break;
		}
	}

}
