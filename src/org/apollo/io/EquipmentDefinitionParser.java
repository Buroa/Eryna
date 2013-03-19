package org.apollo.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apollo.game.model.EquipmentBonuses;
import org.apollo.game.model.def.EquipmentDefinition;

/**
 * A class which parses the {@code data/equipment-[release].dat} file to create
 * an array of {@link EquipmentDefinition} s.
 * @author Graham
 */
public final class EquipmentDefinitionParser {

	/**
	 * The input stream.
	 */
	private final InputStream is;

	/**
	 * Creates the equipment definition parser.
	 * @param is The input stream.
	 */
	public EquipmentDefinitionParser(InputStream is) {
		this.is = is;
	}

	/**
	 * Parses the input stream.
	 * @return The equipment definition array.
	 * @throws IOException if an I/O error occurs.
	 */
	public EquipmentDefinition[] parse() throws IOException {
		final DataInputStream dis = new DataInputStream(is);
		final int count = dis.readShort() & 0xFFFF;
		final EquipmentDefinition[] defs = new EquipmentDefinition[count];
		for (int id = 0; id < count; id++) {
			final int slot = dis.readByte() & 0xFF;
			if (slot != 0xFF) {
				final boolean twoHanded = dis.readBoolean();
				final boolean fullBody = dis.readBoolean();
				final boolean fullHat = dis.readBoolean();
				final boolean fullMask = dis.readBoolean();
				final int attack = dis.readByte() & 0xFF;
				final int strength = dis.readByte() & 0xFF;
				final int defence = dis.readByte() & 0xFF;
				final int ranged = dis.readByte() & 0xFF;
				final int magic = dis.readByte() & 0xFF;
				final int bonusLength = dis.readByte() & 0xFF;
				final double[] bonuses = new double[bonusLength];
				for (int i = 0; i < bonusLength; i++) {
					final double bonus = dis.readDouble();
					bonuses[i] = bonus;
				}
				final EquipmentDefinition def = new EquipmentDefinition(id);
				final EquipmentBonuses equipmentBonuses = new EquipmentBonuses(bonuses);
				def.setLevels(attack, strength, defence, ranged, magic);
				def.setSlot(slot);
				def.setBonuses(equipmentBonuses);
				def.setFlags(twoHanded, fullBody, fullHat, fullMask);
				defs[id] = def;
			}
		}
		return defs;
	}
}
