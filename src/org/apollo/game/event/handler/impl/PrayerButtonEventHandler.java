package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ButtonEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.melee.Prayer;
import org.apollo.game.model.melee.Prayer.Prayers;

/**
 * An {@link EventHandler} for the {@link ButtonEvent}
 * @author Steve
 */
public final class PrayerButtonEventHandler extends EventHandler<ButtonEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ButtonEvent event) {
		final Prayers prayer = Prayers.forId(event.getInterfaceId());
		if (prayer != null) {
			Prayer.togglePrayer(player, prayer);
			ctx.breakHandlerChain();
		}
	}

}
