package org.apollo;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.game.model.Player;
import org.apollo.game.model.World;

/**
 * Loads the shutdown hooks.
 * @author Steve
 */
public final class ServerHooks extends Thread {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ServerHooks.class.getName());

	@Override
	public void run() {
		logger.info("Stopping Apollo...");
		try {
			for (final Player player : World.getWorld().getPlayerRepository())
				player.logout();
		} catch (final Exception e) {
			logger.log(Level.SEVERE, "Error stopping Apollo.", e);
		}
	}
}
