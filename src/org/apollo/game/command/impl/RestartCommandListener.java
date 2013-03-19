package org.apollo.game.command.impl;

import org.apollo.Server;
import org.apollo.ServerHooks;
import org.apollo.game.command.Command;
import org.apollo.game.command.PrivilegedCommandListener;
import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * An {@link PrivilegedCommandListener} for the restart command.
 * @author Steve
 */
public final class RestartCommandListener extends PrivilegedCommandListener {

	/**
	 * Restarts this application.
	 */
	public static final void restart() {
		Server.main(new String[0]);
	}

	/**
	 * Creates the restart command listener.
	 */
	public RestartCommandListener() {
		super(PrivilegeLevel.DEVELOPER);
	}

	@Override
	public void executePrivileged(Player player, Command command) {
		final String[] arguments = command.getArguments();
		new ServerHooks().run();
		if (arguments.length == 1)
			World.getWorld().schedule(new ScheduledTask(Integer.parseInt(arguments[0]), false) {

				@Override
				public void execute() {
					restart();
				}

			});
		else
			restart();
	}

}
