package org.apollo.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apollo.game.event.handler.chain.EventHandlerChainGroup;
import org.apollo.game.model.Position;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.obj.DynamicGameObject;
import org.apollo.game.model.obj.GameObject;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class which parses the {@code object-spawns.xml} file to populate the world
 * with {@link GameObject}s.
 * @author Steve
 */
public final class ObjectSpawnParser {

	/**
	 * The {@link XmlParser} instance.
	 */
	private XmlParser parser;

	/**
	 * The source {@link InputStream}.
	 */
	private final InputStream is;

	/**
	 * Creates the NPC spawn parser.
	 * @param is The source {@link InputStream}.
	 */
	public ObjectSpawnParser(InputStream is) {
		try {
			this.parser = new XmlParser();
		} catch (final SAXException e) {
			this.parser = null;
		}
		this.is = is;
	}

	/**
	 * Parses the XML and produces an array of {@link GameObject}s which are to
	 * be registered to the world..
	 * @return An {@link EventHandlerChainGroup}.
	 */
	public DynamicGameObject[] parse() {
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

		final List<GameObject> objects = new ArrayList<GameObject>();
		final int maxId = ObjectDefinition.count() - 1;

		for (final XmlNode objectNode : rootNode) {

			final int id = Integer.parseInt(objectNode.getAttribute("id"));
			// We simply ignore any ids above the maximum for the current
			// release.
			if (id > maxId)
				continue;

			final XmlNode posNode = objectNode.getChild("position");
			final XmlNode nodeX = posNode.getChild("x");
			final XmlNode nodeY = posNode.getChild("y");

			final String nodeF = objectNode.getAttribute("face");
			final String nodeG = objectNode.getAttribute("type");
			final String nodeH = objectNode.getAttribute("del");
			final String nodeI = objectNode.getAttribute("rep");
			int face = 0;
			int type = 10;
			boolean delete = false;
			boolean replace = false;

			if (nodeF != null)
				face = Integer.parseInt(nodeF);

			if (nodeG != null)
				type = Integer.parseInt(nodeG);

			if (nodeH != null)
				delete = Boolean.parseBoolean(nodeH);

			if (nodeI != null)
				replace = Boolean.parseBoolean(nodeI);

			final int x = Integer.parseInt(nodeX.getValue());
			final int y = Integer.parseInt(nodeY.getValue());

			Position pos;

			final XmlNode nodeZ = posNode.getChild("height");
			if (nodeZ != null) {
				final int height = Integer.parseInt(nodeZ.getValue());
				pos = new Position(x, y, height);
			}
			else
				pos = new Position(x, y);

			final DynamicGameObject go = new DynamicGameObject(id, pos, type, face, delete, replace);
			objects.add(go);
		}

		return objects.toArray(new DynamicGameObject[objects.size()]);
	}

}
