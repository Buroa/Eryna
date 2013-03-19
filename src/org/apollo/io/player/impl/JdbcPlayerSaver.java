package org.apollo.io.player.impl;

import org.apollo.game.model.Player;
import org.apollo.io.player.PlayerSaver;

/**
 * An {@link PlayerSaver} that utilizes a {@code jdbc} database.
 * @author Steve
 */
public final class JdbcPlayerSaver implements PlayerSaver {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.io.player.PlayerSaver#savePlayer(org.apollo.game.model.Player)
	 */
	@Override
	public void savePlayer(Player player) throws Exception {
	}
}
