package org.apollo;

import java.math.BigInteger;

/**
 * The core settings of this application.
 * @author Buroa
 */
public final class ServerSettings {

	/**
	 * The flag indicating if rsa is enabled.
	 */
	private boolean rsaEnabled = false;

	/**
	 * The flag indicating if crc checking is enabled.
	 */
	private boolean crcEnabled = false;

	/**
	 * The server name.
	 */
	private String serverName = "Apollo";

	/**
	 * The flag indicating the game packet queue is enabled.
	 */
	private boolean packetQueueEnabled = true;

	/**
	 * The rsa modulus integer.
	 */
	private BigInteger rsaModulusInteger;

	/**
	 * The rsa exponent integer.
	 */
	private BigInteger rsaExponentInteger;

	/**
	 * The show connections flag.
	 */
	private boolean showConnections;

	/**
	 * Gets the rsa exponent integer.
	 * @return The rsa exponent integer.
	 */
	public BigInteger getRsaExponentInteger() {
		return rsaExponentInteger;
	}

	/**
	 * Gets the rsa modulus integer.
	 * @return The rsa modulus integer.
	 */
	public BigInteger getRsaModulusInteger() {
		return rsaModulusInteger;
	}

	/**
	 * Gets the server name.
	 * @return The server name.
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Gets the crc enabled flag.
	 * @return True if crc is enabled, false if otherwise.
	 */
	public boolean isCrcEnabled() {
		return crcEnabled;
	}

	/**
	 * Gets the packet queued flag.
	 * @return True if the packet queue is enabled, false if otherwise.
	 */
	public boolean isPacketQueueEnabled() {
		return packetQueueEnabled;
	}

	/**
	 * Gets the rsa enabled flag.
	 * @return True if rsa is enabled, false if otherwise.
	 */
	public boolean isRsaEnabled() {
		return rsaEnabled;
	}

	/**
	 * Gets the show connections flag.
	 * @return True if showing connections, false if otherwise.
	 */
	public boolean isShowingConnections() {
		return showConnections;
	}

	/**
	 * Sets the crc enabled flag.
	 * @param crcEnabled True if crc is enabled, false if otherwise.
	 */
	public void setCrcEnabled(boolean crcEnabled) {
		this.crcEnabled = crcEnabled;
	}

	/**
	 * Sets the packet queue flag.
	 * @param packetQueueEnabled True if the packet queue is enabled, false if
	 * otherwise.
	 */
	public void setPacketQueueEnabled(boolean packetQueueEnabled) {
		this.packetQueueEnabled = packetQueueEnabled;
	}

	/**
	 * Sets the rsa enabled flag.
	 * @param rsaEnabled True if rsa is enabled, false if otherwise.
	 */
	public void setRsaEnabled(boolean rsaEnabled) {
		this.rsaEnabled = rsaEnabled;
	}

	/**
	 * Sets the rsa encryption keys.
	 * @param rsaModulusInteger The rsa modulus integer.
	 * @param rsaExponentInteger The rsa exponent integer.
	 */
	public void setRsaEncryption(BigInteger rsaModulusInteger, BigInteger rsaExponentInteger) {
		this.rsaModulusInteger = rsaModulusInteger;
		this.rsaExponentInteger = rsaExponentInteger;
	}

	/**
	 * Sets the server name.
	 * @param serverName The server name.
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * Sets the show connections flag.
	 * @param showConnections True if show connections, false if otherwise.
	 */
	public void setShowConnections(boolean showConnections) {
		this.showConnections = showConnections;
	}

}
