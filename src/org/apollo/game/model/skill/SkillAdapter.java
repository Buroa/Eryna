package org.apollo.game.model.skill;

import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;

/**
 * An adapter for the {@link SkillListener}.
 * @author Graham
 */
public abstract class SkillAdapter implements SkillListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.model.skill.SkillListener#levelledUp(org.apollo.game.
	 * model.SkillSet, int,
	 * org.apollo.game.model.Skill)
	 */
	@Override
	public void levelledUp(SkillSet set, int id, Skill skill) {
		/* empty */
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.model.skill.SkillListener#skillsUpdated(org.apollo.game
	 * .model.SkillSet)
	 */
	@Override
	public void skillsUpdated(SkillSet set) {
		/* empty */
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.model.skill.SkillListener#skillUpdated(org.apollo.game
	 * .model.SkillSet, int,
	 * org.apollo.game.model.Skill)
	 */
	@Override
	public void skillUpdated(SkillSet set, int id, Skill skill) {
		/* empty */
	}
}
