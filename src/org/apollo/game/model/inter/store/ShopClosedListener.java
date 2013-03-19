package org.apollo.game.model.inter.store;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.InterfaceAdapter;
import org.apollo.game.model.inter.InterfaceListener;

/**
 * An {@link InterfaceListener} for the shops.
 * @author Steve
 */
public final class ShopClosedListener extends InterfaceAdapter {

	/**
	 * The shop.
	 */
	private final Shop shop;

	/**
	 * Create a shop listener for the player.
	 * @param shop The shop.
	 */
	public ShopClosedListener(Shop shop) {
		this.shop = shop;
	}

	@Override
	public void interfaceClosed(Player player, boolean manually) {
		shop.removeViewer(player);
		player.getSettings().setShop(null);
	}
}
