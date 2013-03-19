package org.apollo.io.player.impl;

import org.apollo.io.player.PlayerLoader;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.security.PlayerCredentials;

/**
 * An {@link PlayerLoader} that utilizes a {@code jdbc} database.
 * @author Steve
 */
public final class JdbcPlayerLoader implements PlayerLoader {

	/*
	 * (non-Javadoc)
	 * @see org.apollo.io.player.PlayerLoader#loadPlayer(org.apollo.security.
	 * PlayerCredentials)
	 */
	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception {
		return null;
	}
}
