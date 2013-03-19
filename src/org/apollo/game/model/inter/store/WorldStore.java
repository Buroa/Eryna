package org.apollo.game.model.inter.store;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;

/**
 * An shop that holds all the {@link World}'s store that a {@link Player} can
 * purchase items in.
 * @author Steve
 */
public final class WorldStore {

	/**
	 * Holds the shops.
	 */
	private final Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

	/**
	 * Adds a shop to the world store.
	 * @param id The shop id.
	 * @param shop The shop.
	 * @return True if shop was added, false if already added.
	 */
	public boolean addShop(int id, Shop shop) {
		if (shops.get(id) == null) {
			shops.put(id, shop);
			return true;
		}
		else
			return false;
	}

	/**
	 * Gets the shop.
	 * @param shop The shop id.
	 * @return The shop that belongs to the id.
	 */
	public Shop getShop(int shop) {
		return shops.get(shop);
	}

	/**
	 * Gets the world shops.
	 * @return The world shops.
	 */
	public Map<Integer, Shop> getShops() {
		return shops;
	}

	/**
	 * Open up a shop.
	 * @param player The player.
	 * @param shop The shop.
	 */
	public void openShop(Player player, int shop) {
		if (getShop(shop) != null) {
			final Shop store = getShop(shop);
			player.send(new SetInterfaceTextEvent(3901, store.getName()));
			player.send(new UpdateItemsEvent(3823, player.getInventory().getItems()));
			player.send(new UpdateItemsEvent(3900, store.getItems().getItems()));
			player.getSettings().setShop(store);
			store.addViewer(player);
			// TODO interface listener enhancements may allow single-instance
			// shop listeners?
			player.getInterfaceSet().openWindowWithSidebar(new ShopClosedListener(store), 3824, 3822);
		}
	}
}
