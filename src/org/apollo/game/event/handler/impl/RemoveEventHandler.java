package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An event handler which removes equipped items.
 * @author Graham
 */
public final class RemoveEventHandler extends EventHandler<ItemActionEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		if (event.getOption() == 1 && event.getInterfaceId() == SynchronizationInventoryListener.EQUIPMENT_ID) {
			final Inventory inventory = player.getInventory();
			final Inventory equipment = player.getEquipment();
			final int slot = event.getSlot();
			if (inventory.freeSlots() < 1) {
				inventory.forceCapacityExceeded();
				ctx.breakHandlerChain();
				return;
			}
			final Item item = equipment.get(slot);
			boolean removed = true;
			inventory.stopFiringEvents();
			equipment.stopFiringEvents();
			try {
				equipment.set(slot, null);
				final Item tmp = item;
				inventory.add(item.getId(), item.getAmount());
				if (tmp == null) {
					removed = false;
					equipment.set(slot, tmp);
				}
			} finally {
				inventory.startFiringEvents();
				equipment.startFiringEvents();
			}
			if (removed) {
				inventory.forceRefresh(); // TODO find out the specific slot
				// that got used?
				equipment.forceRefresh();
			}
			else
				inventory.forceCapacityExceeded();
		}
	}
}
