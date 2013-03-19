package org.apollo.game.model.melee;

import org.apollo.game.event.impl.SetInterfaceItemModelEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.SwitchTabInterfaceEvent;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;

/**
 * A class which contains combat interfaces.
 * @author Steve
 */
public final class CombatInterfaces {

	/**
	 * Sends the weapon interface.
	 * @param player The player.
	 * @param id The weapon id.
	 * @param name The weapon name.
	 * @param genericName The generic weapon name.
	 */
	public static void sendWeapon(Player player, int id, String name, String genericName) {
		if (name.equals("unarmed")) {
			player.send(new SwitchTabInterfaceEvent(0, 5855));
			player.send(new SetInterfaceTextEvent(5857, genericName));
		}
		else if (name.endsWith("whip")) {
			player.send(new SwitchTabInterfaceEvent(0, 12290));
			player.send(new SetInterfaceItemModelEvent(12291, id, 200));
			player.send(new SetInterfaceTextEvent(12293, genericName));
		}
		else if (name.endsWith("Scythe")) {
			player.send(new SwitchTabInterfaceEvent(0, 776));
			player.send(new SetInterfaceItemModelEvent(777, id, 200));
			player.send(new SetInterfaceTextEvent(779, genericName));
		}
		else if (name.endsWith("bow") || name.startsWith("crystal bow") || name.startsWith("toktz-xil-ul")) {
			player.send(new SwitchTabInterfaceEvent(0, 1764));
			player.send(new SetInterfaceItemModelEvent(1765, id, 200));
			player.send(new SetInterfaceTextEvent(1767, genericName));
		}
		else if (name.endsWith("maul")) {
			player.send(new SwitchTabInterfaceEvent(0, 425));
			player.send(new SetInterfaceItemModelEvent(426, id, 200));
			player.send(new SetInterfaceTextEvent(428, genericName));
		}
		else if (name.endsWith("Staff") || name.endsWith("staff")) {
			player.send(new SwitchTabInterfaceEvent(0, 328));
			player.send(new SetInterfaceItemModelEvent(329, id, 200));
			player.send(new SetInterfaceTextEvent(331, genericName));
		}
		else if (name.endsWith("dart")) {
			player.send(new SwitchTabInterfaceEvent(0, 4446));
			player.send(new SetInterfaceItemModelEvent(4447, id, 200));
			player.send(new SetInterfaceTextEvent(4449, genericName));
		}
		else if (name.endsWith("dagger")) {
			player.send(new SwitchTabInterfaceEvent(0, 2276));
			player.send(new SetInterfaceItemModelEvent(2277, id, 200));
			player.send(new SetInterfaceTextEvent(2279, genericName));
		}
		else if (name.endsWith("pickaxe")) {
			player.send(new SwitchTabInterfaceEvent(0, 5570));
			player.send(new SetInterfaceItemModelEvent(5571, id, 200));
			player.send(new SetInterfaceTextEvent(5573, genericName));
		}
		else if (genericName.endsWith("axe") || genericName.endsWith("battleaxe")) {
			player.send(new SwitchTabInterfaceEvent(0, 1698));
			player.send(new SetInterfaceItemModelEvent(1699, id, 200));
			player.send(new SetInterfaceTextEvent(1701, genericName));
		}
		else if (name.endsWith("halberd")) {
			player.send(new SwitchTabInterfaceEvent(0, 8460));
			player.send(new SetInterfaceItemModelEvent(8461, id, 200));
			player.send(new SetInterfaceTextEvent(8463, genericName));
		}
		else {
			player.send(new SwitchTabInterfaceEvent(0, 2423));
			player.send(new SetInterfaceItemModelEvent(2424, id, 200));
			player.send(new SetInterfaceTextEvent(2426, genericName));
		}
	}

	/**
	 * Updates the weapon.
	 * @param player The player.
	 */
	public static void updateWeapon(Player player) {
		final Item item = player.getEquipment().get(EquipmentConstants.WEAPON);
		String itemName = null;
		String genericName = null;
		if (item != null) {
			if (item.getDefinition().getName() != null) {
				itemName = item.getDefinition().getName().toLowerCase();
				genericName = item.getDefinition().getName();
			}
			else {
				itemName = "unarmed";
				genericName = "Unarmed";
			}
		}
		else {
			itemName = "unarmed";
			genericName = "Unarmed";
		}
		if (item != null)
			sendWeapon(player, item.getId(), itemName, genericName);
		else
			sendWeapon(player, -1, itemName, genericName);
	}

}