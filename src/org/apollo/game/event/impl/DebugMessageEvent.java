package org.apollo.game.event.impl;

/**
 * An event which is sent to the client with a server-side debug message.
 * @author Steve
 */
public final class DebugMessageEvent extends ServerMessageEvent {

	/**
	 * The debug type.
	 */
	private final int type;

	/**
	 * Creates the {@link ServerMessageEvent}.
	 * @param message The message.
	 * @param type The debug type.
	 */
	public DebugMessageEvent(String message, int type) {
		super(message);
		this.type = type;
	}

	/**
	 * Gets the debug type.
	 * @return The debug type.
	 */
	public int getType() {
		return type;
	}
}
