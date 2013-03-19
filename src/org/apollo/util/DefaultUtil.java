package org.apollo.util;

import org.apollo.game.command.Command;
import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.security.PlayerCredentials;

/**
 * A default utility.
 * @author Steve
 */
public final class DefaultUtil {

	/**
	 * The server player.
	 */
	private static final Player server = new Player(new PlayerCredentials("Server", "", 1327848063, 0), new Position(0, 0));

	/**
	 * Initializes the server player.
	 */
	static {
		server.setPrivilegeLevel(PrivilegeLevel.OWNER);
	}

	/**
	 * Creates the command.
	 * @param command The command to parse.
	 * @return The parsed {@link Command}.
	 */
	public static Command createCommand(String command) {
		final String str = command;
		final String[] components = str.split(" ");
		final String name = components[0];
		final String[] arguments = new String[components.length - 1];
		System.arraycopy(components, 1, arguments, 0, arguments.length);
		return new Command(name, arguments);
	}

	/**
	 * Executes a command.
	 * @param command The command.
	 */
	public static void executeCommand(Command command) {
		World.getWorld().getCommandDispatcher().dispatch(server, command);
	}

}
