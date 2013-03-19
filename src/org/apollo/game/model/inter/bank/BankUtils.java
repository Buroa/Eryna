package org.apollo.game.model.inter.bank;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * Contains bank-related utility methods.
 * @author Graham
 */
public final class BankUtils {

	/**
	 * Deposits an item into the player's bank.
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The id.
	 * @param amount The amount.
	 * @return {@code false} if the chain should be broken.
	 */
	public static boolean deposit(Player player, int slot, int id, int amount) {
		if (amount == 0)
			return true;
		final Inventory inventory = player.getInventory();
		final Inventory bank = player.getBank();
		final Item item = inventory.get(slot);
		final int newId = ItemDefinition.noteToItem(item.getId());
		if (bank.freeSlots() == 0 && !bank.contains(item.getId())) {
			bank.forceCapacityExceeded();
			return true;
		}
		int removed;
		if (amount > 1)
			inventory.stopFiringEvents();
		try {
			removed = inventory.remove(item.getId(), amount);
		} finally {
			if (amount > 1)
				inventory.startFiringEvents();
		}
		if (amount > 1)
			inventory.forceRefresh();
		bank.add(newId, removed);
		return true;
	}

	/**
	 * Deposits an item into the player's bank.
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The id.
	 * @param amount The amount.
	 * @param inventory The inventory.
	 * @return {@code false} if the chain should be broken.
	 */
	public static boolean deposit(Player player, int slot, int id, int amount, Inventory inventory) {
		if (amount == 0)
			return true;
		final Inventory bank = player.getBank();
		final Item item = inventory.get(slot);
		final int newId = ItemDefinition.noteToItem(item.getId());
		if (bank.freeSlots() == 0 && !bank.contains(item.getId())) {
			bank.forceCapacityExceeded();
			return true;
		}
		int removed;
		if (amount > 1)
			inventory.stopFiringEvents();
		try {
			removed = inventory.remove(item.getId(), amount);
		} finally {
			if (amount > 1)
				inventory.startFiringEvents();
		}
		if (amount > 1)
			inventory.forceRefresh();
		bank.add(newId, removed);
		return true;
	}

	/**
	 * Deposits the current inventory.
	 * @param player The player
	 */
	public static void depositInventory(Player player) {
		final Inventory inventory = player.getInventory().clone();
		player.getInventory().clear();
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.get(i);
			if (item != null)
				BankUtils.deposit(player, i, item.getId(), item.getAmount(), inventory);
		}
	}

	/**
	 * Opens a player's bank.
	 * @param player The player.
	 */
	public static void openBank(Player player) {
		final InventoryListener invListener = new SynchronizationInventoryListener(player,
				BankConstants.SIDEBAR_INVENTORY_ID);
		final InventoryListener bankListener = new SynchronizationInventoryListener(player,
				BankConstants.BANK_INVENTORY_ID);
		player.getInventory().addListener(invListener);
		player.getBank().addListener(bankListener);
		player.getInventory().forceRefresh();
		player.getBank().forceRefresh();
		final InterfaceListener interListener = new BankInterfaceListener(invListener, bankListener);
		player.getInterfaceSet().openWindowWithSidebar(interListener, BankConstants.BANK_WINDOW_ID,
				BankConstants.SIDEBAR_ID);
	}

	/**
	 * Withdraws an item from a player's bank.
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The id.
	 * @param amount The amount.
	 * @return {@code false} if the chain should be broken.
	 */
	public static boolean withdraw(Player player, int slot, int id, int amount) {
		if (amount == 0)
			return true;
		final Inventory inventory = player.getInventory();
		final Inventory bank = player.getBank();
		if (slot < 0 || slot >= bank.capacity())
			return false;
		final Item item = bank.get(slot);
		if (item == null || item.getId() != id)
			return false;
		if (amount >= item.getAmount())
			amount = item.getAmount();
		final int newId = player.getSettings().isWithdrawingNotes() ? ItemDefinition.itemToNote(item.getId()) : item.getId();
		if (inventory.freeSlots() == 0 && !(inventory.contains(newId) && ItemDefinition.forId(newId).isStackable())) {
			inventory.forceCapacityExceeded();
			return true;
		}
		final int remaining = inventory.add(newId, amount);
		bank.stopFiringEvents();
		try {
			bank.remove(item.getId(), amount - remaining);
			bank.shift();
		} finally {
			bank.startFiringEvents();
		}
		bank.forceRefresh();
		return true;
	}

	/**
	 * Default private constructor to prevent insantiation.
	 */
	private BankUtils() {
	}
}
