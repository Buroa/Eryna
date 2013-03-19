package org.apollo.game.model.inter.trade;

import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.EnterAmountListener;

/**
 * An {@link EnterAmountListener} for withdrawing items.
 * @author Steve
 */
public final class TradeDepositEnterAmountListener implements EnterAmountListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * Creates the bank withdraw amount listener.
	 * @param player The player.
	 * @param id The id.
	 */
	public TradeDepositEnterAmountListener(Player player, int id) {
		this.player = player;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.game.model.inter.EnterAmountListener#amountEntered(int)
	 */
	@Override
	public void amountEntered(int amount) {
		if (player.getInterfaceSet().contains(TradeConstants.TRADE_INTERFACE_ID))
			player.getSettings().getTradeSession().offerItem(new Item(id, amount));
	}
}
