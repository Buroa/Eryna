package org.apollo.net.codec.world;

import java.util.HashMap;

import org.apollo.security.WorldAuthentication;

/**
 * Represents a world request.
 * @author Steve
 */
@SuppressWarnings("rawtypes")
public final class WorldRequest {

	/**
	 * The authentication.
	 */
	private final WorldAuthentication auth;

	/**
	 * The module.
	 */
	private final String module;

	/**
	 * The command.
	 */
	private final String command;

	/**
	 * The data if provided any.
	 */
	private HashMap data;

	/**
	 * Creates a new world request.
	 * @param module The module requested.
	 * @param command The command requested.
	 * @param data The data being sent.
	 * @param auth The authentication.
	 */
	public WorldRequest(String module, String command, HashMap data, WorldAuthentication auth) {
		this.module = module;
		this.command = command;
		this.data = data;
		this.auth = auth;
	}

	/**
	 * Creates a new world request.
	 * @param module The module requested.
	 * @param command The command requested.
	 * @param auth The authentication.
	 */
	public WorldRequest(String module, String command, WorldAuthentication auth) {
		this(module, command, null, auth);
	}

	/**
	 * Gets the world authentication.
	 * @return The world authentication.
	 */
	public WorldAuthentication getAuthentication() {
		return auth;
	}

	/**
	 * Gets the command requested.
	 * @return The command requested.
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Gets the data sent.
	 * @return The data sent.
	 */
	public HashMap getData() {
		if (data == null)
			data = new HashMap();
		return data;
	}

	/**
	 * Gets the module requested.
	 * @return The module requested.
	 */
	public String getModule() {
		return module;
	}

	@Override
	public String toString() {
		return WorldRequest.class.getName() + " [auth=" + auth + ", module=" + module + ", command=" + command + ", data=" + getData() + "]";
	}

}
