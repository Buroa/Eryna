package org.apollo.game.model.inter.bank;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inv.InventoryListener;

/**
 * An {@link InterfaceListener} which removes the {@link InventoryListener}s
 * when the bank is closed. Also listens for
 * button clicks on the bank window.
 * @author Graham
 * @author Chris Fletcher
 */
final class BankInterfaceListener implements InterfaceListener {

	/**
	 * The withdraw as item button id.
	 */
	private static final int WITHDRAW_AS_ITEM = 5387;

	/**
	 * The withdraw as note button id.
	 */
	private static final int WITHDRAW_AS_NOTE = 5386;

	/**
	 * The inventory listener.
	 */
	private final InventoryListener invListener;

	/**
	 * The bank listener.
	 */
	private final InventoryListener bankListener;

	/**
	 * Creates the bank interface listener.
	 * @param invListener The inventory listener.
	 * @param bankListener The bank listener.
	 */
	public BankInterfaceListener(InventoryListener invListener, InventoryListener bankListener) {
		this.invListener = invListener;
		this.bankListener = bankListener;
	}

	@Override
	public boolean buttonClicked(Player player, int button) {
		final boolean withdrawAsNotes = button == WITHDRAW_AS_NOTE;

		if (withdrawAsNotes || button == WITHDRAW_AS_ITEM) {
			player.getSettings().setWithdrawingNotes(withdrawAsNotes);
			return true;
		}

		return false;
	}

	@Override
	public void interfaceClosed(Player player, boolean manually) {
		player.getInventory().removeListener(invListener);
		player.getBank().removeListener(bankListener);
	}
}
