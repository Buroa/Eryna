package org.apollo.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apollo.game.event.Event;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.chain.EventHandlerChain;
import org.apollo.game.event.handler.chain.EventHandlerChainGroup;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class which parses the {@code events.xml} file to produce
 * {@link EventHandlerChainGroup}s.
 * @author Graham
 */
public final class EventHandlerChainParser {

	/**
	 * The {@link XmlParser} instance.
	 */
	private final XmlParser parser;

	/**
	 * The source {@link InputStream}.
	 */
	private final InputStream is;

	/**
	 * Creates the event chain parser.
	 * @param is The source {@link InputStream}.
	 * @throws SAXException if a SAX error occurs.
	 */
	public EventHandlerChainParser(InputStream is) throws SAXException {
		this.parser = new XmlParser();
		this.is = is;
	}

	/**
	 * Parses the XML and produces a group of {@link EventHandlerChain}s.
	 * @return An {@link EventHandlerChainGroup}.
	 * @throws IOException if an I/O error occurs.
	 * @throws SAXException if a SAX error occurs.
	 * @throws ClassNotFoundException if a class was not found.
	 * @throws InstantiationException if a class could not be instantiated.
	 * @throws IllegalAccessException if a class was accessed illegally.
	 */
	@SuppressWarnings("unchecked")
	public EventHandlerChainGroup parse() throws IOException, SAXException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		final XmlNode rootNode = parser.parse(is);
		if (!rootNode.getName().equals("events"))
			throw new IOException("root node name is not 'events'");
		final Map<Class<? extends Event>, EventHandlerChain<?>> chains = new HashMap<Class<? extends Event>, EventHandlerChain<?>>();
		for (final XmlNode eventNode : rootNode) {
			if (!eventNode.getName().equals("event"))
				throw new IOException("only expected nodes named 'event' beneath the root node");
			final XmlNode typeNode = eventNode.getChild("type");
			if (typeNode == null)
				throw new IOException("no node named 'type' beneath current event node");
			final XmlNode chainNode = eventNode.getChild("chain");
			if (chainNode == null)
				throw new IOException("no node named 'chain' beneath current event node");
			final String eventClassName = typeNode.getValue();
			if (eventClassName == null)
				throw new IOException("type node must have a value");
			final Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName(eventClassName);
			final List<EventHandler<?>> handlers = new ArrayList<EventHandler<?>>();
			for (final XmlNode handlerNode : chainNode) {
				if (!handlerNode.getName().equals("handler"))
					throw new IOException("only expected nodes named 'handler' beneath the root node");
				final String handlerClassName = handlerNode.getValue();
				if (handlerClassName == null)
					throw new IOException("handler node must have a value");
				final Class<? extends EventHandler<?>> handlerClass = (Class<? extends EventHandler<?>>) Class
						.forName(handlerClassName);
				final EventHandler<?> handler = handlerClass.newInstance();
				handlers.add(handler);
			}
			final EventHandler<?>[] handlersArray = handlers.toArray(new EventHandler<?>[handlers.size()]);
			@SuppressWarnings("rawtypes")
			final EventHandlerChain<?> chain = new EventHandlerChain(handlersArray);
			chains.put(eventClass, chain);
		}
		return new EventHandlerChainGroup(chains);
	}
}
