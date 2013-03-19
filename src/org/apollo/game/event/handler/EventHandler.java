package org.apollo.game.event.handler;

import org.apollo.game.event.Event;
import org.apollo.game.model.Player;

/**
 * A class which handles events.
 * @param <E> The type of event this class handles.
 * @author Graham
 */
public abstract class EventHandler<E extends Event> {

	/**
	 * Handles an event.
	 * @param ctx The context.
	 * @param player The player.
	 * @param event The event.
	 */
	public abstract void handle(EventHandlerContext ctx, Player player, E event);
}
