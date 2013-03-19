package org.apollo.game.model.inter.bank;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.InterfaceAdapter;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inv.InventoryListener;

/**
 * An implementation of the {@link InterfaceListener} to revert deposit box
 * actions and remove the associated inventory
 * listeners upon closing an interface.
 * @author Chris Fletcher
 */
public final class DepositBoxInterfaceListener extends InterfaceAdapter {

	/**
	 * The inventory listener.
	 */
	private final InventoryListener invListener;

	/**
	 * The deposit box listener.
	 */
	private final InventoryListener boxListener;

	/**
	 * Creates a new {@link DepositBoxInterfaceListener}.
	 * @param invListener The inventory listener.
	 * @param boxListener The box listener.
	 */
	DepositBoxInterfaceListener(InventoryListener invListener, InventoryListener boxListener) {
		this.invListener = invListener;
		this.boxListener = boxListener;
	}

	@Override
	public void interfaceClosed(Player player, boolean manually) {
		DepositBoxUtils.revert(player);
		player.getInventory().removeListener(invListener);
		player.getDepositBox().removeListener(boxListener);
	}
}
