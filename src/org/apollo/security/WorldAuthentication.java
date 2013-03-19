package org.apollo.security;

/**
 * Represents a world authentication.
 * @author Steve
 */
public final class WorldAuthentication {

	/**
	 * The world key.
	 */
	private final String key;

	/**
	 * The username.
	 */
	private final String user;

	/**
	 * The password.
	 */
	private final String password;

	/**
	 * Creates a new world authentication.
	 * @param key The world key.
	 * @param user The username.
	 * @param password The password.
	 */
	public WorldAuthentication(String key, String user, String password) {
		this.key = key;
		this.user = user;
		this.password = password;
	}

	/**
	 * Gets the world key.
	 * @return The world key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the password.
	 * @return The password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the user.
	 * @return The user.
	 */
	public String getUser() {
		return user;
	}

	@Override
	public String toString() {
		return WorldAuthentication.class.getName() + " [key=" + key + ", user=" + user + ", password=" + password + "]";
	}

}
