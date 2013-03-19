package org.apollo.game.model;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.tools.EquipmentConstants;

/**
 * Contains character-related bonuses.
 * @author Steve
 */
public final class CharacterBonuses {

	/**
	 * The character.
	 */
	private final Character character;

	/**
	 * The equipment bonuses.
	 */
	private EquipmentBonuses bonuses = new EquipmentBonuses(new double[18]);

	/**
	 * The first update.
	 */
	private boolean firstUpdate = true;

	/**
	 * Creates the character bonuses.
	 * @param character The character.
	 */
	public CharacterBonuses(Character character) {
		this.character = character;
	}

	/**
	 * Returns the 317 bonus format.
	 * @param bonus The bonus.
	 * @return The 317 format bonus.
	 */
	private int bonusForId(int bonus) {
		switch (bonus) {
		case EquipmentBonuses.DEFENSE_SUMMONING:
		case EquipmentBonuses.STRENGTH_RANGE:
		case EquipmentBonuses.ABSORB_MAGIC:
		case EquipmentBonuses.ABSORB_MELEE:
		case EquipmentBonuses.ABSORB_RANGE:
			return -1;
		case EquipmentBonuses.PRAYER:
			return 11;
		case EquipmentBonuses.STRENGTH_MELEE:
			return 12;
		}
		return bonus;
	}

	/**
	 * Refreshes the bonuses for this player.
	 */
	public void forceRefresh() {
		final Inventory equipment = character.getEquipment();
		EquipmentBonuses newBonuses = new EquipmentBonuses(new double[18]);

		for (int i = 0; i < equipment.capacity(); i++)
			if (equipment.get(i) != null) {
				final Item equiped = equipment.get(i);
				newBonuses = newBonuses.append(equiped.getBonuses());
			}

		writeBonus(bonuses, newBonuses);
		this.bonuses = newBonuses;
	}

	/**
	 * Gets the equipment bonuses.
	 * @return The equipment bonuses.
	 */
	public EquipmentBonuses getBonuses() {
		return bonuses;
	}

	/**
	 * Writes the bonuses.
	 */
	private void writeBonus(EquipmentBonuses oldBonuses, EquipmentBonuses newBonuses) {
		int i = 0;
		String text = "";
		for (final double bonus : bonuses) {
			final int bonusId = bonusForId(i);
			if (bonusId != -1)
				if (oldBonuses.getBonus(i) != newBonuses.getBonus(i) || firstUpdate) {
					text = EquipmentConstants.BONUS_NAMES[i] + ": " + Integer.toString((int) bonus);
					character.send(new SetInterfaceTextEvent(1675 + bonusId, text));
				}
			i++;
		}
		firstUpdate = false;
	}

}