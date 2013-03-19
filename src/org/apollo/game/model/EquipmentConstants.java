package org.apollo.game.model;

import org.apollo.game.model.def.ItemDefinition;

/**
 * Contains equipment-related constants.
 * @author Graham
 */
public final class EquipmentConstants {

	/**
	 * The hat slot.
	 */
	public static final int HAT = 0;

	/**
	 * The cape slot.
	 */
	public static final int CAPE = 1;

	/**
	 * The amulet slot.
	 */
	public static final int AMULET = 2;

	/**
	 * The weapon slot.
	 */
	public static final int WEAPON = 3;

	/**
	 * The chest slot.
	 */
	public static final int CHEST = 4;

	/**
	 * The shield slot.
	 */
	public static final int SHIELD = 5;

	/**
	 * The legs slot.
	 */
	public static final int LEGS = 7;

	/**
	 * The hands slot.
	 */
	public static final int HANDS = 9;

	/**
	 * The feet slot.
	 */
	public static final int FEET = 10;

	/**
	 * The ring slot.
	 */
	public static final int RING = 12;

	/**
	 * The arrows slot.
	 */
	public static final int ARROWS = 13;

	/**
	 * Gets the attack animation.
	 * @param character
	 * The character.
	 * @return The attack animation
	 */
	public static int getAttackAnim(Character character) {
		if (!character.isControlling())
			return 422;
		try {
			final int id = character.getEquipment().get(EquipmentConstants.WEAPON)
					.getId();
			final String name = ItemDefinition.forId(id).getName().toLowerCase();

			if (name.contains("dharok"))
				return 2067;
			if (name.contains("whip"))
				return 1658;
			if (name.contains("shortbow") || name.contains("Shortbow")
					|| name.contains("longbow") || name.contains("crystal"))
				return 426;
			// if (name.contains("crossbow") || name.contains("c'bow"))
			// return 4230;
			if (name.contains("granite"))
				return 1665;
			if (name.contains("godsword"))
				return 7041;
			if (name.contains("knife") || name.contains("dart")
					|| name.contains("javelin") || name.contains("thrownaxe"))
				return 806;
			if (name.contains("halberd"))
				return 440;
			if (name.startsWith("dragon dagger"))
				return 402;
			if (name.endsWith("dagger") || name.contains("dagger"))
				return 412;
			if (name.contains("2h sword"))
				return 406;
			if (name.contains("sword") || name.contains("scim"))
				return 451;
			switch (id) {
			case 6522:
				return 2614;
			case 4726: // guthan
				return 2080;
			case 4747: // torag
				return 0x814;
			case 4710: // ahrim
				return 406;
			case 4755: // verac
				return 2062;
			case 4734: // karil
				return 2075;
			case 4151:
				return 1658;
			}
			return 422;

		} catch (final Exception e) {
			return 422;
		}
	}

	/**
	 * Gets the attack speed.
	 * @param character The character.
	 * @return The attack speed.
	 */
	public static int getAttackSpeed(Character character) {
		if (!character.isControlling())
			return 5;

		try {
			final int id = character.getEquipment().get(EquipmentConstants.WEAPON)
					.getId();
			final String name = ItemDefinition.forId(id).getName().toLowerCase();

			if (name.contains("dart") || name.contains("knife"))
				return 1;

			if (name.contains("dagger") || name.contains("dagger")
					|| name.endsWith("sword") && name.endsWith("longsword")
					|| name.endsWith("whip") || name.contains("slayer staff")
					|| name.contains("ancient staff")
					|| name.endsWith("scimitar") || name.endsWith("claws")
					|| name.endsWith("shortbow") || name.contains("karil"))
				return 4;

			if (name.endsWith("battleaxe") || name.endsWith("warmhammer")
					|| name.startsWith("ahrim") || name.contains("javelin"))
				return 5;

			if (name.contains("2h") || name.endsWith("halberd")
					|| name.endsWith("maul") || name.startsWith("dharok")
					|| name.startsWith("staff"))
				return 7;

			switch (id) {
			case 4212:
				return 5;
			case 2415:
			case 2416:
			case 2417:
			case 6522:
				return 4;
			case 6526:
			case 6538:
				return 6;
			case 6528:
			case 6540:
				return 7;
			case 2883:
				return 8;
			case 11235:
				return 9;
			}
		} catch (final Exception e) {
			return 5;
		}
		return 5;
	}

	/**
	 * Returns the run animation for the player's weapon id.
	 * @param appearance
	 * The players appearance.
	 * @param weaponId
	 * The weapon id.
	 * @return The run animation.
	 */
	public static int getRunAnimation(Appearance appearance, int weaponId) {
		switch (weaponId) {
		case 4151:
			return 1661;
		case 1419:
			return 9739;
		case 4934:
			return 2077;
		default:
			return appearance.getRunAnimation();
		}
	}

	/**
	 * Returns the stand animation for the player's weapon id.
	 * @param appearance
	 * The players appearance.
	 * @param weaponId
	 * The weapon id.
	 * @return The stand animation.
	 */
	public static int getStandAnimation(Appearance appearance, int weaponId) {
		switch (weaponId) {
		case 837:
			return 2237;
		case 4084:
			return 1642;
		case 4718:
			return 2065;
		case 746:
		case 667:
		case 35:
		case 2402:
		case 8100:
		case 16033:
		case 15936:
		case 15914:
			return 7047;
		case 1307:
		case 1309:
		case 1311:
		case 1313:
		case 1315:
		case 1317:
		case 1319:
		case 6609:
		case 7158:
			return 1131;
		case 4755:
			return 2061;
		case 4734:
			return 2074;
		case 4710:
		case 4726:
			return 802;
		case 4153:
			return 1662;
		case 6528:
			return 0x811;
		case 4565:
			return 1836;
		case 1419:
			return 847;
		case 4934:
			return 2074;
		default:
			return appearance.getStandAnimation();
		}
	}

	/**
	 * Returns the walk animation for the player's weapon id.
	 * @param appearance
	 * The players appearance.
	 * @param weaponId
	 * The weapon id.
	 * @return The walk animation.
	 */
	public static int getWalkAnimation(Appearance appearance, int weaponId) {
		switch (weaponId) {
		case 3565:
			return 1836;
		case 4153:
			return 1663;
		case 4755:
			return 824;
		case 1419:
			return 9738;
		case 4934:
			return 2076;
		default:
			return appearance.getWalkAnimation();
		}
	}

	/**
	 * Default private constructor to prevent instantiation;.
	 */
	private EquipmentConstants() {
	}
}
