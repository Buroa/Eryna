package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.RegionLoadEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.region.RegionMusic;

/**
 * An {@link EventHandler} for the {@link RegionLoadEvent}
 * @author Steve
 */
public final class RegionMusicEventHandler extends EventHandler<RegionLoadEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, RegionLoadEvent event) {
		RegionMusic.playMusic(player);
	}

}