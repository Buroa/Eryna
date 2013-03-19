package org.apollo.game.model.inter.store.impl;

import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.SlottedItem;
import org.apollo.game.model.inter.store.ShopPaymentType;

/**
 * An {@link ShopPaymentType} that lets players buy with coins.
 * @author Steve
 */
public final class CoinsShopPaymentType extends ShopPaymentType {

	@Override
	public boolean buyItem(Player player, SlottedItem item) {
		final int price = buyValue(item.getItem().getId()) * item.getItem().getAmount();
		if (player.getInventory().contains(995, price))
			return player.getInventory().remove(995, price) == price;
		else
			return false;
	}

	@Override
	public int buyValue(int item) {
		return new Item(item).getDefinition().getValue();
	}

	@Override
	public String getName() {
		return "coins";
	}

	@Override
	public boolean sellItem(Player player, Item item) {
		if (item.getId() != 995)
			if (player.getInventory().contains(item)) {
				final int price = sellValue(item.getId()) * item.getAmount();
				player.getInventory().add(995, price);
				return true;
			}
		return false;
	}

	@Override
	public int sellValue(int item) {
		return new Item(item).getDefinition().getValue() / 2;
	}
}
