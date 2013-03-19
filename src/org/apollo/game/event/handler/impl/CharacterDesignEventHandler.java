package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.CharacterDesignEvent;
import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.model.Player;

/**
 * A handler which handles {@link CharacterDesignEvent}s.
 * @author Graham
 */
public final class CharacterDesignEventHandler extends EventHandler<CharacterDesignEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, CharacterDesignEvent event) {
		player.setAppearance(event.getAppearance());
		player.getSettings().setDesignedCharacter(true);
		player.send(new CloseInterfaceEvent());
	}
}
