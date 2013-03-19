package org.apollo.game.model.inter.bank;

import static org.apollo.game.model.inter.bank.DepositBoxConstants.DEPBOX_INVENTORY_ID;
import static org.apollo.game.model.inter.bank.DepositBoxConstants.DEPBOX_WINDOW_ID;
import static org.apollo.game.model.inter.bank.DepositBoxConstants.SIDEBAR_ID;
import static org.apollo.game.model.inter.bank.DepositBoxConstants.SIDEBAR_INVENTORY_ID;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * Contains deposit box-related utility methods.
 * @author Chris Fletcher
 */
public final class DepositBoxUtils {

	/**
	 * Deposits an item from the deposit box to the bank.
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The id.
	 * @param amount The amount.
	 * @return {@code false} if the chain should be broken.
	 */
	public static boolean deposit(Player player, int slot, int id, int amount) {
		if (amount == 0)
			return true;
		final Inventory box = player.getDepositBox();
		int removed;
		if (amount > 1)
			box.stopFiringEvents();
		try {
			removed = box.remove(id, amount);
		} finally {
			if (amount > 1)
				box.startFiringEvents();
		}
		if (amount > 1)
			box.forceRefresh();
		player.getBank().add(id, removed);
		return true;
	}

	/**
	 * Opens a player's deposit box.
	 * @param player The player.
	 */
	public static void openBox(Player player) {
		final InventoryListener invListener = new SynchronizationInventoryListener(player, SIDEBAR_INVENTORY_ID);
		final InventoryListener boxListener = new SynchronizationInventoryListener(player, DEPBOX_INVENTORY_ID);
		player.getInventory().addListener(invListener);
		player.getDepositBox().addListener(boxListener);
		player.getInventory().forceRefresh();
		player.getDepositBox().forceRefresh();
		player.getDepositBox().startFiringEvents();
		final DepositBoxInterfaceListener interListener = new DepositBoxInterfaceListener(invListener, boxListener);
		player.getInterfaceSet().openWindowWithSidebar(interListener, DEPBOX_WINDOW_ID, SIDEBAR_ID);
	}

	/**
	 * Deposits an item into the player's deposit box.
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The id.
	 * @param amount The amount.
	 * @return {@code false} if the chain should be broken.
	 */
	public static boolean put(Player player, int slot, int id, int amount) {
		if (amount == 0)
			return true;
		final Inventory inventory = player.getInventory();
		final Inventory box = player.getDepositBox();
		final int newId = ItemDefinition.noteToItem(id);
		final int freeSpace = player.getBank().freeSpace(newId);
		if (amount > freeSpace) {
			player.getBank().forceCapacityExceeded();
			if (freeSpace <= 0)
				return false;
			amount = freeSpace;
		}
		int removed;
		if (amount > 1)
			inventory.stopFiringEvents();
		try {
			removed = inventory.remove(id, amount);
		} finally {
			if (amount > 1)
				inventory.startFiringEvents();
		}
		if (amount > 1)
			inventory.forceRefresh();
		box.add(id, removed);
		return true;
	}

	/**
	 * Reverts the actions made while the deposit box was open.
	 * @param player The player.
	 */
	static void revert(Player player) {
		final Inventory box = player.getDepositBox();
		final Inventory inventory = player.getInventory();
		// box.stopFiringEvents();
		final int amount = box.size();
		if (amount > 1)
			inventory.stopFiringEvents();
		try {
			for (final Item item : box.getItems())
				if (item != null)
					inventory.add(item);
			box.clear();
		} finally {
			if (amount > 1)
				inventory.startFiringEvents();
		}
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private DepositBoxUtils() {
	}
}
