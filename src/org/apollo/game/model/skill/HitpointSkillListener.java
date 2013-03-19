package org.apollo.game.model.skill;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Character;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;

/**
 * A {@link SkillListener} which sends config values when a player levels up
 * their health.
 * @author Steve
 */
public final class HitpointSkillListener extends SkillAdapter {

	/**
	 * The character.
	 */
	private final Character character;

	/**
	 * Creates the level up listener for the specified character.
	 * @param character The character.
	 */
	public HitpointSkillListener(Character character) {
		this.character = character;
	}

	@Override
	public void levelledUp(SkillSet set, int id, Skill skill) {
		if (id == Skill.HITPOINTS)
			if (character.isControlling())
				((Player) character).send(new SetInterfaceTextEvent(4017, Integer.toString(skill.getMaximumLevel())));
	}

	@Override
	public void skillUpdated(SkillSet set, int id, Skill skill) {
		if (id == Skill.HITPOINTS)
			if (character.isControlling())
				((Player) character).send(new SetInterfaceTextEvent(4016, Integer.toString(skill.getCurrentLevel())));
	}
}
