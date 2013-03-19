package org.apollo.game.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Contains equipment-related bonuses.
 * @author Steve
 */
public final class EquipmentBonuses implements Iterable<Double> {

	/**
	 * The attack stab bonus.
	 */
	public static final int ATTACK_STAB = 0;

	/**
	 * The attack slash bonus.
	 */
	public static final int ATTACK_SLASH = 1;

	/**
	 * The attack crush bonus.
	 */
	public static final int ATTACK_CRUSH = 2;

	/**
	 * The attack magic bonus.
	 */
	public static final int ATTACK_MAGIC = 3;

	/**
	 * The attack range bonus.
	 */
	public static final int ATTACK_RANGE = 4;

	/**
	 * The defense stab bonus.
	 */
	public static final int DEFENSE_STAB = 5;

	/**
	 * The defense slash bonus.
	 */
	public static final int DEFENSE_SLASH = 6;

	/**
	 * The defense crush bonus.
	 */
	public static final int DEFENSE_CRUSH = 7;

	/**
	 * The defense magic bonus.
	 */
	public static final int DEFENSE_MAGIC = 8;

	/**
	 * The defense range bonus.
	 */
	public static final int DEFENSE_RANGE = 9;

	/**
	 * The defense summoning bonus.
	 */
	public static final int DEFENSE_SUMMONING = 10;

	/**
	 * The absorb melee bonus.
	 */
	public static final int ABSORB_MELEE = 11;

	/**
	 * The absorb magic bonus.
	 */
	public static final int ABSORB_MAGIC = 12;

	/**
	 * The absorb range bonus.
	 */
	public static final int ABSORB_RANGE = 13;

	/**
	 * The strength melee bonus.
	 */
	public static final int STRENGTH_MELEE = 14;

	/**
	 * The strength range bonus.
	 */
	public static final int STRENGTH_RANGE = 15;

	/**
	 * The prayer bonus.
	 */
	public static final int PRAYER = 16;

	/**
	 * The magic bonus.
	 */
	public static final int MAGIC = 17;

	/**
	 * The equipment bonuses.
	 */
	private final double[] bonuses;

	/**
	 * Creates equipment bonuses.
	 * @param bonuses The bonuses to set.
	 */
	public EquipmentBonuses(double[] bonuses) {
		this.bonuses = bonuses;
	}

	/**
	 * Appends more equipment bonuses to the current bonuses.
	 * @param bonuses The bonuses to append.
	 * @return The new equipment bonuses.
	 */
	public EquipmentBonuses append(EquipmentBonuses bonuses) {
		final double[] temp = this.bonuses;
		int i = 0;
		for (final double bonus : bonuses)
			temp[i++] += bonus;
		return new EquipmentBonuses(temp);
	}

	/**
	 * Appends a bonus to the specified slot.
	 * @param slot The slot.
	 * @param bonus The bonus to append.
	 * @return The new equipment bonuses.
	 */
	public EquipmentBonuses append(int slot, int bonus) {
		final double[] temp = this.bonuses;
		temp[slot] += bonus;
		return new EquipmentBonuses(temp);
	}

	/**
	 * Gets the absorb magic bonus.
	 * @return The absorb magic bonus.
	 */
	public double getAbsorbMagic() {
		return bonuses[ABSORB_MAGIC];
	}

	/**
	 * Gets the absorb melee bonus.
	 * @return The absorb melee bonus.
	 */
	public double getAbsorbMelee() {
		return bonuses[ABSORB_MELEE];
	}

	/**
	 * Gets the absorb range bonus.
	 * @return The absorb range bonus.
	 */
	public double getAbsorbRange() {
		return bonuses[ABSORB_RANGE];
	}

	/**
	 * Gets the attack crush bonus.
	 * @return The attack crush bonus.
	 */
	public double getAttackCrush() {
		return bonuses[ATTACK_CRUSH];
	}

	/**
	 * Gets the attack magic bonus.
	 * @return The attack magic bonus.
	 */
	public double getAttackMagic() {
		return bonuses[ATTACK_MAGIC];
	}

	/**
	 * Gets the attack range bonus.
	 * @return The attack range bonus.
	 */
	public double getAttackRange() {
		return bonuses[ATTACK_RANGE];
	}

	/**
	 * Gets the attack slash bonus.
	 * @return The attack slash bonus.
	 */
	public double getAttackSlash() {
		return bonuses[ATTACK_SLASH];
	}

	/**
	 * Gets the attack stab bonus.
	 * @return The attack stab bonus.
	 */
	public double getAttackStab() {
		return bonuses[ATTACK_STAB];
	}

	/**
	 * Gets the bonus.
	 * @param bonus The bonus.
	 * @return The bonus.
	 */
	public double getBonus(int bonus) {
		return bonuses[bonus];
	}

	/**
	 * Gets the defense crush bonus.
	 * @return The defense crush bonus.
	 */
	public double getDefenseCrush() {
		return bonuses[DEFENSE_CRUSH];
	}

	/**
	 * Gets the defense magic bonus.
	 * @return The defense magic bonus.
	 */
	public double getDefenseMagic() {
		return bonuses[DEFENSE_MAGIC];
	}

	/**
	 * Gets the defense range bonus.
	 * @return The defense range bonus.
	 */
	public double getDefenseRange() {
		return bonuses[DEFENSE_RANGE];
	}

	/**
	 * Gets the defense slash bonus.
	 * @return The defense slash bonus.
	 */
	public double getDefenseSlash() {
		return bonuses[DEFENSE_SLASH];
	}

	/**
	 * Gets the defense stab bonus.
	 * @return The defense stab bonus.
	 */
	public double getDefenseStab() {
		return bonuses[DEFENSE_STAB];
	}

	/**
	 * Gets the defense summoning bonus.
	 * @return The defense summoning bonus.
	 */
	public double getDefenseSummoning() {
		return bonuses[DEFENSE_SUMMONING];
	}

	/**
	 * Gets the magic bonus.
	 * @return The magic bonus.
	 */
	public double getMagic() {
		return bonuses[MAGIC];
	}

	/**
	 * Gets the prayer bonus.
	 * @return The prayer bonus.
	 */
	public double getPrayer() {
		return bonuses[PRAYER];
	}

	/**
	 * Gets the strength melee bonus.
	 * @return The strength melee bonus.
	 */
	public double getStrengthMelee() {
		return bonuses[STRENGTH_MELEE];
	}

	/**
	 * Gets the strength range bonus.
	 * @return The strength range bonus.
	 */
	public double getStrengthRange() {
		return bonuses[STRENGTH_RANGE];
	}

	@Override
	public Iterator<Double> iterator() {
		final List<Double> temp = new ArrayList<Double>();
		for (final double bonus : bonuses)
			temp.add(bonus);
		return temp.iterator();
	}

	/**
	 * Sets the equipment bonus for the specified slot.
	 * @param slot The slot.
	 * @param bonus The bonus to set.
	 * @return The new equipment bonuses.
	 */
	public EquipmentBonuses set(int slot, int bonus) {
		final double[] temp = this.bonuses;
		temp[slot] = bonus;
		return new EquipmentBonuses(temp);
	}
}