package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ChatEvent;
import org.apollo.game.model.Player;

/**
 * An event handler which verifies chat events.
 * @author Graham
 */
public final class ChatVerificationHandler extends EventHandler<ChatEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, ChatEvent event) {
		final int color = event.getTextColor();
		final int effects = event.getTextEffects();
		if (color < 0 || color > 11 || effects < 0 || effects > 5)
			ctx.breakHandlerChain();
		if (player.getSettings().isMuted())
			ctx.breakHandlerChain();
	}
}
