package org.apollo.game;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which handles the logic for each pulse of the {@link GameService}.
 * @author Graham
 */
public final class GamePulseHandler implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(GamePulseHandler.class.getName());

	/**
	 * The {@link GameService}.
	 */
	private final GameService service;

	/**
	 * Creates the game pulse handler object.
	 * @param service The {@link GameService}.
	 */
	GamePulseHandler(GameService service) {
		this.service = service;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			final long startTime = System.nanoTime();
			service.pulse();
			final long endTime = System.nanoTime();
			final long totalTime = (endTime - startTime) / 1000000;
			if (totalTime >= 600)
				logger.severe("Not enough processing power is available to run this game.");
			else if (totalTime >= 400)
				logger.warning("You should think about upgrading this servers processing power.");
			else if (totalTime >= 200)
				logger.info("Your server is running at " + totalTime + "ms cycles.");
		} catch (final Throwable t) {
			logger.log(Level.SEVERE, "Exception during pulse.", t);
		}
	}
}
