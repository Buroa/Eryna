package org.apollo.game.model.inter.trade;

import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.event.impl.UpdateSlottedItemsEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.SlottedItem;
import org.apollo.game.model.inv.InventoryAdapter;

/**
 * @author Steve
 */
public final class OfferedItemsContainerListener extends InventoryAdapter {

	private final Player player;

	private final Player acquaintance;

	/**
	 * @param player
	 * @param acquaintance
	 */
	public OfferedItemsContainerListener(Player player, Player acquaintance) {
		this.player = player;
		this.acquaintance = acquaintance;
	}

	@Override
	public void itemsUpdated(Inventory container) {
		player.send(new UpdateItemsEvent(3415, container.getItems()));
		acquaintance.send(new UpdateItemsEvent(3416, container.getItems()));
	}

	@Override
	public void itemUpdated(Inventory container, int slot, Item item) {
		final SlottedItem si = new SlottedItem(slot, item);
		player.send(new UpdateSlottedItemsEvent(3415, si));
		acquaintance.send(new UpdateSlottedItemsEvent(3416, si));
	}
}