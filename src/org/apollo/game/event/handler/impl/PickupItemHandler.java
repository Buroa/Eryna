package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PickupItemEvent;
import org.apollo.game.model.GroundItem;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.region.Chunk;
import org.apollo.game.model.region.RegionManager;

/**
 * An {@link EventHandler} for the group item instance.
 * @author Steve
 */
public final class PickupItemHandler extends EventHandler<PickupItemEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, PickupItemEvent event) {
		final RegionManager regionManager = World.getWorld().getRegionManager();
		final Position position = new Position(event.getX(), event.getY(), player.getPosition().getHeight());
		final Chunk chunk = regionManager.getChunkByPosition(player.getPosition());
		for (final GroundItem item : chunk.getItems())
			if (item.getItem().getId() == event.getItemId())
				if (item.getPosition().equals(position))
					if (item.continued(player.getName()))
						if (player.getInventory().add(item.getItem()) == null)
							World.getWorld().unregister(item);
		ctx.breakHandlerChain();
	}
}