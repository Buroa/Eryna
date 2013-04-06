package org.apollo.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.skill.SkillListener;

/**
 * Represents the set of the player's skills.
 * @author Graham
 */
public final class SkillSet {

	/**
	 * The number of skills.
	 */
	public static final int SKILL_COUNT = 21;

	/**
	 * The maximum allowed experience.
	 */
	public static final double MAXIMUM_EXP = 200000000;

	/**
	 * Gets the minimum experience required for the specified level.
	 * @param level The level.
	 * @return The minimum experience.
	 */
	public static double getExperienceForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * Gets the minimum level to get the specified experience.
	 * @param experience The experience.
	 * @return The minimum level.
	 */
	public static int getLevelForExperience(double experience) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= 120; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= experience + 1)
				return lvl;
		}
		return 120;
	}

	/**
	 * A list of skill listeners.
	 */
	private final List<SkillListener> listeners = new ArrayList<SkillListener>();

	/**
	 * The skills.
	 */
	private final Skill[] skills = new Skill[SKILL_COUNT];

	/**
	 * A flag indicating if events are being fired.
	 */
	private boolean firingEvents = true;

	/**
	 * Sets the skill set.
	 */
	public SkillSet() {
		init();
	}

	/**
	 * Adds experience to the specified skill.
	 * @param id The skill id.
	 * @param experience The amount of experience.
	 */
	public void addExperience(int id, double experience) {
		checkBounds(id);
		final Skill old = skills[id];
		double newExperience = old.getExperience() + experience;
		if (newExperience > MAXIMUM_EXP)
			newExperience = MAXIMUM_EXP;
		int newCurrentLevel = old.getCurrentLevel();
		final int newMaximumLevel = getLevelForExperience(newExperience);
		final int delta = newMaximumLevel - old.getMaximumLevel();
		if (delta > 0)
			newCurrentLevel += delta;
		setSkill(id, new Skill(newExperience, newCurrentLevel, newMaximumLevel));
		if (delta > 0)
			// here so it gets updated skill
			notifyLevelledUp(id);
	}

	/**
	 * Adds a listener.
	 * @param listener The listener to add.
	 */
	public void addListener(SkillListener listener) {
		listeners.add(listener);
	}

	/**
	 * Checks the bounds of the id.
	 * @param id The id.
	 */
	private void checkBounds(int id) {
		if (id < 0 || id >= skills.length)
			throw new IndexOutOfBoundsException();
	}

	/**
	 * Forces this skill set to refresh.
	 */
	public void forceRefresh() {
		notifySkillsUpdated();
	}

	/**
	 * Gets the combat level for this skill set.
	 * @return The combat level.
	 */
	public int getCombatLevel() {
		final int attack = skills[Skill.ATTACK].getMaximumLevel();
		final int defence = skills[Skill.DEFENCE].getMaximumLevel();
		final int strength = skills[Skill.STRENGTH].getMaximumLevel();
		final int hitpoints = skills[Skill.HITPOINTS].getMaximumLevel();
		final int prayer = skills[Skill.PRAYER].getMaximumLevel();
		final int ranged = skills[Skill.RANGED].getMaximumLevel();
		final int magic = skills[Skill.MAGIC].getMaximumLevel();
		int combat = (int) ((defence + hitpoints + Math.floor(prayer / 2)) * 0.253) + 1;
		final double melee = (attack + strength) * 0.325;
		final double ranger = Math.floor(ranged * 1.5) * 0.325;
		final double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage)
			combat += melee;
		else if (ranger >= melee && ranger >= mage)
			combat += ranger;
		else if (mage >= melee && mage >= ranger)
			combat += mage;
		return combat < 154 ? combat : 154;
	}

	/**
	 * Gets a skill by its id.
	 * @param id The id.
	 * @return The skill.
	 */
	public Skill getSkill(int id) {
		checkBounds(id);
		return skills[id];
	}

	/**
	 * Gets the total level for this skill set.
	 * @return The total level.
	 */
	public int getTotalLevel() {
		int total = 0;
		for (final Skill skill : skills)
			total += skill.getMaximumLevel();
		return total;
	}

	/**
	 * Increases the skill by the amount.
	 * @param id The skill id.
	 * @param add The skill amount to add.
	 */
	public void increaseSkill(int id, int add) {
		setSkill(id, new Skill(getSkill(id).getExperience(), getSkill(id).getCurrentLevel() + add, getSkill(id)
				.getMaximumLevel()));
	}

	/**
	 * Initialises the skill set.
	 */
	private void init() {
		for (int i = 0; i < skills.length; i++)
			if (i == Skill.HITPOINTS)
				skills[i] = new Skill(1154, 10, 10);
			else
				skills[i] = new Skill(0, 1, 1);
	}

	/**
	 * Normalizes the skills in this set.
	 */
	public void normalize() {
		for (int i = 0; i < skills.length; i++) {
			int cur = skills[i].getCurrentLevel();
			final int max = skills[i].getMaximumLevel();
			if (cur > max)
				cur--;
			else if (max > cur)
				cur++;
			else
				continue;
			setSkill(i, new Skill(skills[i].getExperience(), cur, max));
		}
	}

	/**
	 * Normalizes the skills in this set.
	 * @param character The character to normalize.
	 */
	public void normalize(Character character) {
		for (int i = 0; i < skills.length; i++) {
			int cur = skills[i].getCurrentLevel();
			final int max = skills[i].getMaximumLevel();
			if (cur > max)
				cur--;
			else if (max > cur) {
				if (i == Skill.HITPOINTS) {
					// if (character.getPrayers().contains(Prayers.RAPID_HEAL))
					// cur += 2;
				}
				else if (i != Skill.PRAYER) {
					// if
					// (character.getPrayers().contains(Prayers.RAPID_RESTORE))
					// cur += 2;
				} else
					cur++;
			} else
				continue;
			setSkill(i, new Skill(skills[i].getExperience(), cur, max));
		}
	}

	/**
	 * Notifies listeners that a skill has been levelled up.
	 * @param id The skill's id.
	 */
	private void notifyLevelledUp(int id) {
		checkBounds(id);
		if (firingEvents)
			for (final SkillListener listener : listeners)
				listener.levelledUp(this, id, skills[id]);
	}

	/**
	 * Notifies listeners that the skills in this listener have been updated.
	 */
	private void notifySkillsUpdated() {
		if (firingEvents)
			for (final SkillListener listener : listeners)
				listener.skillsUpdated(this);
	}

	/**
	 * Notifies listeners that a skill has been updated.
	 * @param id The skill's id.
	 */
	private void notifySkillUpdated(int id) {
		checkBounds(id);
		if (firingEvents)
			for (final SkillListener listener : listeners)
				listener.skillUpdated(this, id, skills[id]);
	}

	/**
	 * Removes all the listeners.
	 */
	public void removeAllListeners() {
		listeners.clear();
	}

	/**
	 * Removes experience from the specified skill.
	 * @param id The skill id.
	 * @param experience The amount of experience.
	 */
	public void removeExperience(int id, double experience) {
		checkBounds(id);
		final Skill old = skills[id];
		double newExperience = old.getExperience() - experience;
		if (newExperience > MAXIMUM_EXP)
			newExperience = MAXIMUM_EXP;
		final int level = getLevelForExperience(newExperience);
		setSkill(id, new Skill(newExperience, level, level));
		notifySkillUpdated(id);
	}

	/**
	 * Removes a listener.
	 * @param listener The listener to remove.
	 */
	public void removeListener(SkillListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Sets a skill.
	 * @param id The skill id.
	 * @param experience The amount of experience.
	 */
	public void setSkill(int id, double experience) {
		checkBounds(id);
		final int level = getLevelForExperience(experience);
		setSkill(id, new Skill(experience, level, level));
		notifySkillUpdated(id);
	}

	/**
	 * Sets a skill.
	 * @param id The id.
	 * @param skill The skill.
	 */
	public void setSkill(int id, Skill skill) {
		checkBounds(id);
		skills[id] = skill;
		notifySkillUpdated(id);
	}

	/**
	 * Gets the number of skills.
	 * @return The number of skills.
	 */
	public int size() {
		return skills.length;
	}

	/**
	 * Re-enables the firing of events.
	 */
	public void startFiringEvents() {
		firingEvents = true;
	}

	/**
	 * Stops events from being fired.
	 */
	public void stopFiringEvents() {
		firingEvents = false;
	}
}
