package org.apollo.game.model.inter.bank;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.EnterAmountListener;

/**
 * An {@link EnterAmountListener} for depositing items from the deposit box to
 * the bank.
 * @author Chris Fletcher
 */
public final class DepositBoxEnterAmountListener implements EnterAmountListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The item slot.
	 */
	private final int slot;

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * Creates a new deposit box enter amount listener.
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The item id.
	 */
	public DepositBoxEnterAmountListener(Player player, int slot, int id) {
		this.player = player;
		this.slot = slot;
		this.id = id;
	}

	@Override
	public void amountEntered(int amount) {
		if (player.getInterfaceSet().contains(DepositBoxConstants.DEPBOX_WINDOW_ID))
			DepositBoxUtils.deposit(player, slot, id, amount);
	}
}
