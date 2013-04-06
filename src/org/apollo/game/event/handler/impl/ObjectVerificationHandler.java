package org.apollo.game.event.handler.impl;

import java.util.Collection;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ObjectActionEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.obj.GameObject;
import org.apollo.game.model.region.Region;

/**
 * An {@link EventHandler} for the {@link ObjectActionEvent}'s.
 * @author Steve
 */
public final class ObjectVerificationHandler extends EventHandler<ObjectActionEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ObjectActionEvent event) {
		boolean found = false;

		// manual objects here. some reason its not in the region
		if (event.getId() == 2295)
			if (event.getPosition().equals(new Position(2474, 3435)))
				return;

		final Region region = player.getRegion();
		final Collection<GameObject> objects = region.getObjects(event.getPosition());
		for (final GameObject object : objects)
			if (object.getId() == event.getId()) {
				found = true;
				break;
			}

		if (!found)
			ctx.breakHandlerChain();
	}
}
