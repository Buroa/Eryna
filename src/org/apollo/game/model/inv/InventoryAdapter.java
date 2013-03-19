package org.apollo.game.model.inv;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;

/**
 * An adapter for the {@link InventoryListener}.
 * @author Graham
 */
public abstract class InventoryAdapter implements InventoryListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.model.inv.InventoryListener#capacityExceeded(org.apollo
	 * .game.model.Inventory)
	 */
	@Override
	public void capacityExceeded(Inventory inventory) {
		/* empty */
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.model.inv.InventoryListener#itemsUpdated(org.apollo.game
	 * .model.Inventory)
	 */
	@Override
	public void itemsUpdated(Inventory inventory) {
		/* empty */
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.model.inv.InventoryListener#itemUpdated(org.apollo.game
	 * .model.Inventory, int,
	 * org.apollo.game.model.Item)
	 */
	@Override
	public void itemUpdated(Inventory inventory, int slot, Item item) {
		/* empty */
	}
}
