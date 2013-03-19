package org.apollo.net.codec.world;

/**
 * Represents a single world reponse.
 * @author Steve
 */
public final class WorldResponse {

	/**
	 * The no such module response.
	 */
	public static final WorldResponse NO_SUCH_MODULE;

	/**
	 * The no such command response.
	 */
	public static final WorldResponse NO_SUCH_COMMAND;

	/**
	 * The key mismatch response.
	 */
	public static final WorldResponse KEY_MISMATCH;

	/**
	 * The authentication required response.
	 */
	public static final WorldResponse AUTHENTICATION_REQUIRED;

	/**
	 * Creates common module responses.
	 */
	static {
		NO_SUCH_MODULE = new WorldResponse("no such module", false);
		NO_SUCH_COMMAND = new WorldResponse("no such command", false);
		KEY_MISMATCH = new WorldResponse("key mismatch", false);
		AUTHENTICATION_REQUIRED = new WorldResponse("authentication required", false);
	}

	/**
	 * The success flag.
	 */
	private final boolean success;

	/**
	 * The world response.
	 */
	private final String response;

	/**
	 * Creates a new world response.
	 * @param success The success flag.
	 */
	public WorldResponse(boolean success) {
		this(null, success);
	}

	/**
	 * Creates a new world response.
	 * @param response The world response.
	 */
	public WorldResponse(String response) {
		this(response, true);
	}

	/**
	 * Creates a new world response.
	 * @param response The world response.
	 * @param success The success flag.
	 */
	public WorldResponse(String response, boolean success) {
		this.response = response;
		this.success = success;
	}

	/**
	 * Gets the response.
	 * @return The response.
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * Gets the success flag.
	 * @return True if successful, false if otherwise.
	 */
	public boolean isSuccessful() {
		return success;
	}

}
