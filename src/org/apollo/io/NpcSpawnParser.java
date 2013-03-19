package org.apollo.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apollo.game.event.handler.chain.EventHandlerChainGroup;
import org.apollo.game.model.Npc;
import org.apollo.game.model.Position;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class which parses the {@code npc-spawns.xml} file to populate the world
 * with {@link Npc}s.
 * @author Chris Fletcher
 */
public final class NpcSpawnParser {

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
	public NpcSpawnParser(InputStream is) {
		try {
			this.parser = new XmlParser();
		} catch (final SAXException e) {
			this.parser = null;
		}
		this.is = is;
	}

	/**
	 * Parses the XML and produces an array of {@link Npc}s which are to be
	 * registered to the world..
	 * @return An {@link EventHandlerChainGroup}.
	 */
	public Npc[] parse() {
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

		final List<Npc> npcs = new ArrayList<Npc>();
		final int maxId = NpcDefinition.count() - 1;

		for (final XmlNode npcNode : rootNode) {

			final int id = Integer.parseInt(npcNode.getAttribute("id"));
			// We simply ignore any ids above the maximum for the current
			// release.
			if (id > maxId)
				continue;

			final XmlNode posNode = npcNode.getChild("position");
			final XmlNode nodeX = posNode.getChild("x");
			final XmlNode nodeY = posNode.getChild("y");

			final String nodeR = npcNode.getAttribute("rwalk");
			boolean rwalk = false;

			if (nodeR != null)
				rwalk = Boolean.parseBoolean(nodeR);

			final String nodeF = npcNode.getAttribute("face");
			int face = 0;

			if (nodeF != null)
				face = Integer.parseInt(nodeF);

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

			final Npc npc = new Npc(id, pos);
			npc.setFace(face);
			npc.setRandomWalking(rwalk);
			npcs.add(npc);
		}

		return npcs.toArray(new Npc[npcs.size()]);
	}

}
