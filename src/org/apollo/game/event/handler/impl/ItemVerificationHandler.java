package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.InventoryItemEvent;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inter.bank.DepositBoxConstants;
import org.apollo.game.model.inter.store.Shop;
import org.apollo.game.model.inter.store.ShopInventory;
import org.apollo.game.model.inter.trade.TradeSession;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An {@link EventHandler} which verifies {@link ItemActionEvent}s.
 * @author Chris Fletcher
 */
public final class ItemVerificationHandler extends EventHandler<InventoryItemEvent> {

	/**
	 * Gets the inventory based on the interface id.
	 * @param interfaceId The interface id.
	 * @return The proper inventory.
	 * @throws IllegalArgumentException if the interface id is not legal.
	 */
	private static Inventory interfaceToInventory(Player player, int interfaceId) {
		switch (interfaceId) {
		case SynchronizationInventoryListener.INVENTORY_ID:
		case BankConstants.SIDEBAR_INVENTORY_ID:
		case DepositBoxConstants.SIDEBAR_INVENTORY_ID:
		case 3322:
		case 3823:
			return player.getInventory();
		case TradeSession.TRADE_INVENTORY_ID:
			final TradeSession tradeSession = player.getSettings().getTradeSession();
			if (tradeSession != null)
				return tradeSession.getItems();
		case SynchronizationInventoryListener.EQUIPMENT_ID:
			return player.getEquipment();
		case BankConstants.BANK_INVENTORY_ID:
			return player.getBank();
		case DepositBoxConstants.DEPBOX_INVENTORY_ID:
			return player.getDepositBox();
		case ShopInventory.SHOP_INVENTORY_ID:
			final Shop shop = player.getSettings().getShop();
			if (shop != null)
				return shop.getItems();
		default:
			throw new IllegalArgumentException("unknown interface id: " + interfaceId);
		}
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, InventoryItemEvent event) {
		/*
		 * Ignore these inventories, they are handled by plugins.
		 */
		if (event.getInterfaceId() == 2274)
			return;

		/*
		 * Acquire the proper inventory for the interface id. This will throw an
		 * exception if the interface id
		 * is unknown, causing the handler chain to break and the event to be
		 * discarded.
		 */
		final Inventory inventory = interfaceToInventory(player, event.getInterfaceId());

		/*
		 * We check if the slot is in bounds; not negative and not equal to or
		 * higher than the inventory's
		 * capacity.
		 */
		final int slot = event.getSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		/*
		 * Lastly, we acquire the item at the specified slot and see if its id
		 * matches the one specified by
		 * the client.
		 */
		final Item item = inventory.get(slot);
		if (item == null || item.getId() != event.getId()) {
			ctx.breakHandlerChain();
			return;
		}
	}
}