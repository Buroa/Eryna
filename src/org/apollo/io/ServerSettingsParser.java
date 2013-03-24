package org.apollo.io;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.apollo.ServerSettings;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class which parses the {@code settings.xml} file to produce {@link ServerSettings}.
 * @author Steve
 */
@SuppressWarnings("javadoc")
public final class ServerSettingsParser {
	
	/**
	 * The {@link XmlParser} instance.
	 */
	private XmlParser parser;

	/**
	 * The source {@link InputStream}.
	 */
	private final InputStream is;

	/**
	 * Creates the server settings parser.
	 * @param is The source {@link InputStream}.
	 * @throws SAXException if a SAX error occurs.
	 */
	public ServerSettingsParser(InputStream is) {
		try {
			this.parser = new XmlParser();
		} catch (final SAXException e) {
			this.parser = null;
		}
		this.is = is;
	}
	
	/**
	 * Parses the XML and produces a group of {@link ServerSettings}.
	 * @return An {@link ServerSettings}.
	 */
	public ServerSettings parse() {
		XmlNode rootNode = null;
		try {
			rootNode = parser.parse(is);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!rootNode.getName().equals("settings"))
			; // bad
		
		final ServerSettings settings = new ServerSettings();
		for (final XmlNode eventNode : rootNode) {
			if (eventNode.getName().equals("login")) {
				final String rsaNode = eventNode.getChild("rsa").getAttribute("enabled");
				final String crcNode = eventNode.getChild("crc").getValue();
				final boolean rsaEnabled = Boolean.parseBoolean(rsaNode);
				final boolean crcEnabled = Boolean.parseBoolean(crcNode);
				settings.setRsaEnabled(rsaEnabled);
				settings.setCrcEnabled(crcEnabled);
				if (rsaEnabled) {
					final String rsaModulus = eventNode.getChild("rsa").getChild("modulus").getValue();
					final BigInteger rsaModulusInteger = new BigInteger(rsaModulus);
					final String rsaExponent = eventNode.getChild("rsa").getChild("exponent").getValue();
					final BigInteger rsaExponentInteger = new BigInteger(rsaExponent);
					settings.setRsaEncryption(rsaModulusInteger, rsaExponentInteger);
				}
			} else if (eventNode.getName().equals("general")) {
				final String serverName = eventNode.getChild("name").getValue();
				settings.setServerName(serverName);
			} else if (eventNode.getName().equals("network")) {
				final String showConnectionNode = eventNode.getChild("show-connections").getValue();
				final String packetQueueNode = eventNode.getChild("packet-queue").getValue();
				final boolean showConnections = Boolean.parseBoolean(showConnectionNode);
				final boolean packetQueueEnabled = Boolean.parseBoolean(packetQueueNode);
				settings.setPacketQueueEnabled(packetQueueEnabled);
				settings.setShowConnections(showConnections);
			}
		}
		return settings;
		
	}

}
