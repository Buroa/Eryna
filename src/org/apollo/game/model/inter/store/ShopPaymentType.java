package org.apollo.game.model.inter.store;

import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.SlottedItem;

/**
 * The {@link Shop} payment type.
 * @author Steve
 */
public abstract class ShopPaymentType {

	/**
	 * Buys an item from the store.
	 * @param player The player that is buying the item.
	 * @param item The item to buy from the store.
	 * @return True if the player can purchase the item, false if not.
	 */
	protected abstract boolean buyItem(Player player, SlottedItem item);

	/**
	 * Gets the buy value of the item.
	 * @param item The item that is being checked.
	 * @return The value of the item.
	 */
	protected abstract int buyValue(int item);

	/**
	 * The name of the payment type.
	 * @return The name of the payment type.
	 */
	public abstract String getName();

	/**
	 * Sells a item to the store.
	 * @param player The player that is selling the item.
	 * @param item The item to sell to the store.
	 * @return True if the player can sell the item, false if not.
	 */
	protected abstract boolean sellItem(Player player, Item item);

	/**
	 * Gets the sell value of the item.
	 * @param item The item that is being checked.
	 * @return The value of the item.
	 */
	protected abstract int sellValue(int item);
}
