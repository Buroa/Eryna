package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.trade.TradeConstants;
import org.apollo.game.model.inter.trade.TradeDepositEnterAmountListener;
import org.apollo.game.model.inter.trade.TradeWithdrawEnterAmountListener;

/**
 * An {@link EventHandler} which responds to {@link ItemActionEvent}s for adding
 * items.
 * @author Steve
 */
public class TradeItemActionEventHandler extends EventHandler<ItemActionEvent> {

	/**
	 * Converts an option to an amount.
	 * @param option The option.
	 * @return The amount.
	 */
	private static final int optionToAmount(int option) {
		switch (option) {
		case 1:
			return 1;
		case 2:
			return 5;
		case 3:
			return 10;
		case 4:
			return Integer.MAX_VALUE;
		case 5:
			return -1;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Handles a deposit action.
	 * @param ctx The event handler context.
	 * @param player The player.
	 * @param event The event.
	 */
	private void deposit(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		final int amount = optionToAmount(event.getOption());
		if (amount == -1)
			player.getInterfaceSet().openEnterAmountDialog(new TradeDepositEnterAmountListener(player, event.getId()));
		else {
			player.getSettings().getTradeSession().offerItem(new Item(event.getId(), amount));
			ctx.breakHandlerChain();
		}
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		if (!player.getInterfaceSet().contains(TradeConstants.TRADE_INTERFACE_ID))
			return;
		if (event.getInterfaceId() == 3322)
			deposit(ctx, player, event);
		else if (event.getInterfaceId() == 3415)
			withdraw(ctx, player, event);
	}

	/**
	 * Handles a withdraw action.
	 * @param ctx The event handler context.
	 * @param player The player.
	 * @param event The event.
	 */
	private void withdraw(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		final int amount = optionToAmount(event.getOption());
		if (amount == -1)
			player.getInterfaceSet().openEnterAmountDialog(new TradeWithdrawEnterAmountListener(player, event.getId()));
		else {
			player.getSettings().getTradeSession().removeOffer(new Item(event.getId(), amount));
			ctx.breakHandlerChain();
		}
	}
}
