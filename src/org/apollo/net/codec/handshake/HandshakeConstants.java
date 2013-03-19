package org.apollo.net.codec.handshake;

/**
 * Holds handshake-related constants.
 * @author Graham
 */
public final class HandshakeConstants {

	/**
	 * The id of the game service.
	 */
	public static final int SERVICE_GAME = 14;

	/**
	 * The id of the update service.
	 */
	public static final int SERVICE_UPDATE = 15;

	/**
	 * The id of the player count.
	 */
	public static final int SERVICE_COUNT = 16;

	/**
	 * The id of the world service.
	 */
	public static final int SERVICE_WORLD = 17;

	/**
	 * Default private constructor to prevent instantiation by other classes.
	 */
	private HandshakeConstants() {
	}
}
