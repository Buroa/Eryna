package org.apollo.world;

/**
 * Represents a world module.
 * @author Steve
 */
public final class WorldModule {

	/**
	 * The module.
	 */
	private final String module;

	/**
	 * The authentication flag.
	 */
	private final boolean authentication;

	/**
	 * Creates a new world module.
	 * @param module The module.
	 */
	public WorldModule(String module) {
		this(module, false);
	}

	/**
	 * Creates a new world module.
	 * @param module The module.
	 * @param authentication The authentication.
	 */
	public WorldModule(String module, boolean authentication) {
		this.module = module;
		this.authentication = authentication;
	}

	/**
	 * Gets the module.
	 * @return The module.
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Gets the authentication flag.
	 * @return True if authentication is required, false if otherwise.
	 */
	public boolean isAuthenticationRequired() {
		return authentication;
	}

}
