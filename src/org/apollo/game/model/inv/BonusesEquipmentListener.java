package org.apollo.game.model.inv;

import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;

/**
 * An {@link InventoryListener} which updates the player's equipment bonuses
 * when any items are updated.
 * @author Steve
 */
public final class BonusesEquipmentListener extends InventoryAdapter {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the appearance inventory listener.
	 * @param player The player.
	 */
	public BonusesEquipmentListener(Player player) {
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.model.inv.InventoryAdapter#itemsUpdated(org.apollo.game
	 * .model.Inventory)
	 */
	@Override
	public void itemsUpdated(Inventory inventory) {
		player.getBonuses().forceRefresh();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.model.inv.InventoryAdapter#itemUpdated(org.apollo.game
	 * .model.Inventory, int,
	 * org.apollo.game.model.Item)
	 */
	@Override
	public void itemUpdated(Inventory inventory, int slot, Item item) {
		player.getBonuses().forceRefresh();
		if (slot == EquipmentConstants.WEAPON)
			;
	}

}
