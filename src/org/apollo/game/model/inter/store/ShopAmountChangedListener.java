package org.apollo.game.model.inter.store;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.SlottedItem;
import org.apollo.game.model.inv.InventoryAdapter;

/**
 * The listener for the shop amount change.
 * @author Steve
 */
public final class ShopAmountChangedListener extends InventoryAdapter {

	/**
	 * The shop.
	 */
	private final Shop shop;

	/**
	 * Create a new shop amount changed listener.
	 * @param shop The shop.
	 */
	public ShopAmountChangedListener(Shop shop) {
		this.shop = shop;
	}

	@Override
	public void itemsUpdated(Inventory inventory) {
		shop.refresh();
	}

	@Override
	public void itemUpdated(Inventory container, int slot, Item item) {
		shop.refresh(new SlottedItem(slot, item));
	}
}
