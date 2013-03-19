package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOptionEvent;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;

/**
 * A handler for the {@link ItemOptionEvent}.
 * @author Steve
 */
public final class DropItemEventHandler extends EventHandler<ItemOptionEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemOptionEvent event) {
		if (event.getOption() == 5) {
			final int slot = event.getSlot();
			final Item item = player.getInventory().get(slot);
			if (item != null) {
				final GroundItem groundItem = new GroundItem(player.getName(), item, player.getPosition());
				World.getWorld().register(groundItem);
				player.getInventory().reset(slot);
			}
			else
				ctx.breakHandlerChain();
		}
	}
}