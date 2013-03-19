package org.apollo.game.model.melee;

import org.apollo.game.model.Character;
import org.jruby.embed.ScriptingContainer;

/**
 * A utility that connects to the combat plugin.
 * @author Steve
 */
public final class Melee {

	/**
	 * The container that holds the combat plugin.
	 */
	private static final ScriptingContainer container = new ScriptingContainer();

	/**
	 * The attack class name.
	 */
	private static final String ATTACK_CLASS_NAME = "DefaultCombatDistancedAction";

	/**
	 * The range class name.
	 */
	private static final String RANGE_CLASS_NAME = "RangeDistancedAction";

	/**
	 * The mage class name.
	 */
	private static final String MAGE_CLASS_NAME = "MagicDistancedAction";

	/**
	 * Starts the attack process.
	 * @param source The source.
	 * @param victim The victim.
	 */
	public static void attack(Character source, Character victim) {
		final StringBuilder builder = build(source, victim);
		builder.append("source.start_action " + ATTACK_CLASS_NAME + ".new(source, victim);");
		container.runScriptlet(builder.toString());
	}

	/**
	 * Builds the string responsible for executing the script.
	 * @param source The source.
	 * @param victim The victim.
	 */
	private static StringBuilder build(Character source, Character victim) {
		final StringBuilder builder = new StringBuilder();
		final int source_index = source.getIndex();
		final int victim_index = victim.getIndex();
		builder.append("require 'java';");
		builder.append("source = World.world." + (source.isControlling() ? "player" : "npc") + "_repository.for_index(" + source_index + ");");
		builder.append("victim = World.world." + (victim.isControlling() ? "player" : "npc") + "_repository.for_index(" + victim_index + ");");
		return builder;
	}

	/**
	 * Gets the combat set.
	 * @param source The source.
	 * @return The combat set.
	 */
	private static StringBuilder get_combat_set(Character source) {
		final StringBuilder builder = new StringBuilder();
		final int source_index = source.getIndex();
		builder.append("require 'java';");
		builder.append("source = World.world." + (source.isControlling() ? "player" : "npc") + "_repository.for_index(" + source_index + ");");
		builder.append("combat_set = get_combat_set(source);");
		return builder;
	}

	/**
	 * Gets the attacking flag.
	 * @param source The source.
	 * @return True if the source is attacking, false if otherwise.
	 */
	public static boolean is_attacking(Character source) {
		final StringBuilder builder = get_combat_set(source);
		builder.append("combat_set.attacking");
		return (boolean) container.runScriptlet(builder.toString());
	}

	/**
	 * Starts the mage process.
	 * @param spell The spell id.
	 * @param source The source.
	 * @param victim The victim.
	 */
	public static void mage(int spell, Character source, Character victim) {
		final StringBuilder builder = build(source, victim);
		builder.append("spell = COMBAT_SPELLS[" + spell + "];");
		builder.append("source.start_action " + MAGE_CLASS_NAME + ".new(source, victim, spell);");
		container.runScriptlet(builder.toString());
	}

	/**
	 * Starts the range process.
	 * @param source The source.
	 * @param victim The victim.
	 */
	public static void range(Character source, Character victim) {
		final StringBuilder builder = build(source, victim);
		builder.append("source.start_action " + RANGE_CLASS_NAME + ".new(source, victim);");
		container.runScriptlet(builder.toString());
	}

	/**
	 * Gets the attacking flag.
	 * @param source The source.
	 * @return True if the source is attacking, false if otherwise.
	 */
	public static boolean under_attack(Character source) {
		final StringBuilder builder = get_combat_set(source);
		builder.append("combat_set.embattled");
		return (boolean) container.runScriptlet(builder.toString());
	}

	/**
	 * Default constructor preventing instantation.
	 */
	private Melee() {

	}

}
