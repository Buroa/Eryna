package org.apollo.game.event.handler.impl;

import org.apollo.game.command.Command;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.CommandEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;

/**
 * An {@link EventHandler} which dispatches {@link CommandEvent}s.
 * @author Graham
 */
public final class CommandEventHandler extends EventHandler<CommandEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, CommandEvent event) {
		final String str = event.getCommand();
		final String[] components = str.split(" ");
		final String name = components[0];
		final String[] arguments = new String[components.length - 1];
		System.arraycopy(components, 1, arguments, 0, arguments.length);
		final Command command = new Command(name, arguments);
		World.getWorld().getCommandDispatcher().dispatch(player, command);
	}
}
