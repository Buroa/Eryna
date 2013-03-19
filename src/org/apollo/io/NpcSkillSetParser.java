package org.apollo.io;

import java.io.IOException;
import java.io.InputStream;

import org.apollo.game.model.Npc;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class which parses the {@code npc-skills.xml} file to populate the
 * {@link Npc}s with {@link Skill}s.
 * @author Steve
 */
public final class NpcSkillSetParser {

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
	public NpcSkillSetParser(InputStream is) {
		try {
			this.parser = new XmlParser();
		} catch (final SAXException e) {
			this.parser = null;
		}
		this.is = is;
	}

	/**
	 * Parses the XML and produces an {@link Npc}s with {@link Skill}s.
	 * @return The number of parsed skills.
	 */
	public int parse() {
		int parsed = 0;
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

		final int maxId = NpcDefinition.count() - 1;

		for (final XmlNode npcNode : rootNode) {

			final int id = Integer.parseInt(npcNode.getAttribute("id"));
			// We simply ignore any ids above the maximum for the current
			// release.
			if (id > maxId)
				continue;

			final NpcDefinition definition = NpcDefinition.forId(id);

			if (definition == null)
				continue;

			final XmlNode skillsNode = npcNode.getChild("skills");

			for (final XmlNode skillNode : skillsNode) {
				final int skillId = Skill.getId(skillNode.getName());
				final int level = Integer.parseInt(skillNode.getAttribute("level"));
				final int exp = (int) SkillSet.getExperienceForLevel(level);
				final Skill skill = new Skill(exp, level, level);
				definition.setSkill(skillId, skill);
				parsed++;
			}
		}

		return parsed;
	}

}
