package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.SlottedItem;
import org.apollo.game.model.inter.store.Shop;

/**
 * An {@link EventHandler} that handles {@link ItemActionEvent}
 * @author Steve
 */
public final class ShopItemActionHandler extends EventHandler<ItemActionEvent> {

	/**
	 * Converts an option to an amount.
	 * @param option The option.
	 * @return The amount.
	 */
	private static final int optionToAmount(int option) {
		switch (option) {
		case 1:
			return 0;
		case 2:
			return 1;
		case 3:
			return 5;
		case 4:
			return 10;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Buy a item from the shop.
	 * @param player The player.
	 * @param event The event.
	 */
	private void buy(Player player, ItemActionEvent event) {
		final Item item = new Item(event.getId(), optionToAmount(event.getOption()));
		final Shop shop = player.getSettings().getShop();
		if (event.getOption() == 1) {
			final String name = shop.getPayment().getName();
			final int value = shop.buyValue(item.getId());
			player.sendMessage((item.getDefinition().getName() != null ? item.getDefinition().getName() : "That")
					+ ": currently costs " + value + " " + name + ".");
		}
		else
			shop.buyItem(player, new SlottedItem(event.getSlot(), item));
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		if (player.getInterfaceSet().contains(3824)) {
			if (player.getSettings().getShop() != null)
				switch (event.getInterfaceId()) {
				case 3900:
					buy(player, event);
					break;
				case 3823:
					sell(player, event);
					break;
				}
			ctx.breakHandlerChain();
		}
	}

	/**
	 * Sell a item to the shop.
	 * @param player The player.
	 * @param event The event.
	 */
	private void sell(Player player, ItemActionEvent event) {
		final Item item = new Item(event.getId(), optionToAmount(event.getOption()));
		final Shop shop = player.getSettings().getShop();
		if (event.getOption() == 1) {
			final String name = shop.getPayment().getName();
			final int value = shop.sellValue(item.getId());
			if (value == -1)
				player.sendMessage("This shop will not buy that item from you.");
			else
				player.sendMessage((item.getDefinition().getName() != null ? item.getDefinition().getName() : "That")
						+ ": shop will buy that for " + value + " " + name + ".");
		}
		else
			shop.sellItem(player, item);
	}
}
