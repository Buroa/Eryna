package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.bank.BankDepositEnterAmountListener;
import org.apollo.game.model.inter.bank.DepositBoxConstants;
import org.apollo.game.model.inter.bank.DepositBoxEnterAmountListener;
import org.apollo.game.model.inter.bank.DepositBoxUtils;

/**
 * An {@link EventHandler} which handles deposit box actions of
 * {@link ItemActionEvent}s.
 * @author Chris Fletcher
 */
public final class DepositBoxEventHandler extends EventHandler<ItemActionEvent> {

	/**
	 * Converts an option to an amount for the
	 * {@link #deposit(EventHandlerContext, Player, ItemActionEvent)} method.
	 * @param option The option.
	 * @return The amount.
	 * @throws IllegalArgumentException if the option is not legal.
	 */
	private static final int optionToAmountDeposit(int option) {
		switch (option) {
		case 1:
			return 1;
		case 2:
			return 5;
		case 3:
			return 10;
		case 4:
			return -1;
		case 5:
			return -2;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Converts an option to an amount for the
	 * {@link #put(EventHandlerContext, Player, ItemActionEvent)} method.
	 * @param option The option.
	 * @return The amount.
	 * @throws IllegalArgumentException if the option is not legal.
	 */
	private static final int optionToAmountPut(int option) {
		switch (option) {
		case 1:
			return 1;
		case 2:
			return 5;
		case 3:
			return 10;
		case 4:
			return -2;
		case 5:
			return -1;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Handles a box-to-bank action.
	 * @param ctx The event handler context.
	 * @param player The player.
	 * @param event The event.
	 */
	private void deposit(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		int amount = optionToAmountDeposit(event.getOption());
		if (amount == -1)
			player.getInterfaceSet().openEnterAmountDialog(
					new DepositBoxEnterAmountListener(player, event.getSlot(), event.getId()));
		else {
			final int id = event.getId(), slot = event.getSlot();
			if (amount == -2)
				amount = player.getDepositBox().getItemCount(id, slot);
			if (!DepositBoxUtils.deposit(player, slot, id, amount))
				ctx.breakHandlerChain();
		}
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		if (!player.getInterfaceSet().contains(DepositBoxConstants.DEPBOX_WINDOW_ID))
			return;
		if (event.getInterfaceId() == DepositBoxConstants.SIDEBAR_INVENTORY_ID)
			put(ctx, player, event);
		else if (event.getInterfaceId() == DepositBoxConstants.DEPBOX_INVENTORY_ID)
			deposit(ctx, player, event);
	}

	/**
	 * Handles an inventory-to-box action.
	 * @param ctx The event handler context.
	 * @param player The player.
	 * @param event The event.
	 */
	private void put(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		int amount = optionToAmountPut(event.getOption());
		if (amount == -1)
			player.getInterfaceSet().openEnterAmountDialog(
					new BankDepositEnterAmountListener(player, event.getSlot(), event.getId()));
		else {
			final int id = event.getId(), slot = event.getSlot();
			if (amount == -2)
				amount = player.getInventory().getItemCount(id, slot);
			if (!DepositBoxUtils.put(player, slot, id, amount))
				ctx.breakHandlerChain();
		}
	}
}
