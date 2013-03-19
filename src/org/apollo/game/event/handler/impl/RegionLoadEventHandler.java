package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.RegionLoadEvent;
import org.apollo.game.model.Player;

/**
 * An {@link EventHandler} for the {@link RegionLoadEvent}
 * @author Steve
 */
public final class RegionLoadEventHandler extends EventHandler<RegionLoadEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, RegionLoadEvent event) {
		player.getLocalEventList().clear();
	}
}