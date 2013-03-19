package org.apollo.game.model.skill;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Character;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.game.model.melee.Prayer;

/**
 * A {@link SkillListener} which sends config values when a player levels up
 * prayer.
 * @author Steve
 */
public final class PrayerSkillListener extends SkillAdapter {

	/**
	 * The player.
	 */
	private final Character character;

	/**
	 * Creates the level up listener for the specified player.
	 * @param character The player.
	 */
	public PrayerSkillListener(Character character) {
		this.character = character;
	}

	@Override
	public void levelledUp(SkillSet set, int id, Skill skill) {
		if (id == Skill.PRAYER)
			character.send(new SetInterfaceTextEvent(4013, Integer.toString(skill.getMaximumLevel())));
	}

	@Override
	public void skillUpdated(SkillSet set, int id, Skill skill) {
		if (id == Skill.PRAYER) {
			character.send(new SetInterfaceTextEvent(4012, Integer.toString(skill.getCurrentLevel())));
			if (skill.getCurrentLevel() == 1)
				Prayer.clearPrayers(character, true);
		}
	}
}
