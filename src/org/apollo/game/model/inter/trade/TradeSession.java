package org.apollo.game.model.inter.trade;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.util.TextUtil;

/**
 * @author Steve
 */
public final class TradeSession {

	/**
	 * @author Steve
	 */
	public enum State {
		/**
		 * 
		 */
		TRADING,
		/**
		 * 
		 */
		AWAITING_ACCEPTANCE,
		/**
		 * 
		 */
		CONFIRMING_TRADE,
		/**
		 * 
		 */
		AWAITING_COMFORMATION,
		/**
		 * 
		 */
		FINALIZING,
		/**
		 * 
		 */
		DECLINING;
	}

	/**
	 * The trade inventory id.
	 */
	public static final int TRADE_INVENTORY_ID = 3415;

	private final Player player;

	private final Player acquaintance;

	private final Inventory offeredItems = new Inventory(28);

	private State state = State.TRADING;

	protected TradeSession(Player player, Player acquaintance) {
		this.player = player;
		this.acquaintance = acquaintance;
		offeredItems.addListener(new OfferedItemsContainerListener(player, acquaintance));
		player.getInventory().addListener(new TradeInventoryContainerListener(player));
	}

	/**
	 * 
	 */
	public void accept() {
		final TradeSession as = verifyAcquaintanceSession();
		if (as.state == State.AWAITING_ACCEPTANCE) {
			constructComformationWindow();
			as.constructComformationWindow();
		}
		else if (as.state == State.AWAITING_COMFORMATION)
			finalizeTrade();
		else if (state == State.TRADING) {
			state = State.AWAITING_ACCEPTANCE;
			player.send(new SetInterfaceTextEvent(3431, "Waiting for other player."));
			acquaintance.send(new SetInterfaceTextEvent(3431, "Other player has accepted."));
		}
		else if (state == State.CONFIRMING_TRADE) {
			state = State.AWAITING_COMFORMATION;
			player.send(new SetInterfaceTextEvent(3535, "Waiting for other player."));
			acquaintance.send(new SetInterfaceTextEvent(3535, "Other player has accepted."));
		}
	}

	private void constructComformationWindow() {
		final TradeSession as = verifyAcquaintanceSession();
		state = State.CONFIRMING_TRADE;
		final InterfaceListener pl = player.getInterfaceSet().removeListener();
		player.getInterfaceSet().openWindowWithSidebar(pl, 3443, 3321);
		player.send(new UpdateItemsEvent(3322, player.getInventory().getItems())); // 3557
		// 3558
		if (offeredItems.freeSlots() < 28) {
			final StringBuilder sb = new StringBuilder();
			int amountAdded = 0;
			for (final Item item : offeredItems.getItems())
				if (item != null) {
					if (amountAdded >= 16) {
						sb.append("and more...");
						break;
					}
					sb.append(item.getDefinition().getName());
					final int amount = item.getAmount();
					if (amount > 1) {
						sb.append(" x ");
						if (item.getAmount() > 9_999_999)
							sb.append("@gre@" + TextUtil.formatValue(amount) + "@whi@");
						else if (item.getAmount() > 99_999)
							sb.append("@cya@" + TextUtil.formatValue(amount) + "@whi@");
						else
							sb.append(TextUtil.commify(amount));
						sb.append(" (" + TextUtil.commify(amount) + ")");
					}
					sb.append("\\n");
					amountAdded++;
				}
			player.send(new SetInterfaceTextEvent(3557, sb.toString()));
		}
		if (as.offeredItems.freeSlots() < 28) {
			final StringBuilder sb = new StringBuilder();
			int amountAdded = 0;
			for (final Item item : as.offeredItems.getItems())
				if (item != null) {
					if (amountAdded >= 16) {
						sb.append("and more...");
						break;
					}
					sb.append(item.getDefinition().getName());
					final int amount = item.getAmount();
					if (amount > 1) {
						sb.append(" x ");
						if (item.getAmount() > 9_999_999)
							sb.append("@gre@" + TextUtil.formatValue(amount) + "@whi@");
						else if (item.getAmount() > 99_999)
							sb.append("@cya@" + TextUtil.formatValue(amount) + "@whi@");
						else
							sb.append(TextUtil.commify(amount));
						sb.append(" (" + TextUtil.commify(amount) + ")");
					}
					sb.append("\\n");
					amountAdded++;
				}
			player.send(new SetInterfaceTextEvent(3558, sb.toString()));
		}
	}

	protected void decline() {
		final TradeSession as = verifyAcquaintanceSession();
		final Inventory pi = player.getInventory();
		final Inventory ai = acquaintance.getInventory();
		for (final Item item : offeredItems.getItems())
			if (item != null)
				pi.add(item);
		for (final Item item : as.offeredItems.getItems())
			if (item != null)
				ai.add(item);
		acquaintance.sendMessage("The other player has declined.");
		state = State.DECLINING;
		player.getInterfaceSet().close();
	}

	private void finalizeTrade() {
		final TradeSession as = verifyAcquaintanceSession();
		if (state == State.FINALIZING || as.getState() == State.FINALIZING)
			return;
		state = State.FINALIZING;
		final Inventory pi = player.getInventory();
		final Inventory ai = acquaintance.getInventory();
		if (!pi.hasRoomFor(as.offeredItems.getItems())) {
			player.sendMessage("You do not have enough for room.");
			acquaintance.sendMessage("The other player does not have enough room.");
			for (final Item item : offeredItems.getItems())
				if (item != null)
					pi.add(item);
			for (final Item item : as.offeredItems.getItems())
				if (item != null)
					ai.add(item);
		}
		else if (!ai.hasRoomFor(offeredItems.getItems())) {
			acquaintance.sendMessage("You do not have enough for room.");
			player.sendMessage("The other player does not have enough room.");
			for (final Item item : offeredItems.getItems())
				if (item != null)
					pi.add(item);
			for (final Item item : as.offeredItems.getItems())
				if (item != null)
					ai.add(item);
		}
		else {
			for (final Item item : offeredItems.getItems())
				if (item != null)
					ai.add(item);
			for (final Item item : as.offeredItems.getItems())
				if (item != null)
					pi.add(item);
		}
		player.getInterfaceSet().removeListener();
		acquaintance.getInterfaceSet().removeListener();
		player.getInterfaceSet().close();
		acquaintance.getInterfaceSet().close();
	}

	Player getAcquaintance() {
		return acquaintance;
	}

	/**
	 * Gets the inventory items.
	 * @return The items.
	 */
	public Inventory getItems() {
		return offeredItems;
	}

	Player getPlayer() {
		return player;
	}

	State getState() {
		return state;
	}

	/**
	 * @param item
	 */
	public void offerItem(Item item) {
		final TradeSession as = verifyAcquaintanceSession();
		if (as.state.ordinal() > 1 || state.ordinal() > 1)
			return;
		final int amountRemoved = player.getInventory().remove(item);
		if (amountRemoved < 1)
			return;
		offeredItems.add(item.getId(), amountRemoved);
		if (as.state == State.AWAITING_ACCEPTANCE || state == State.AWAITING_ACCEPTANCE) {
			state = State.TRADING;
			as.state = State.TRADING;
			player.send(new SetInterfaceTextEvent(3431, ""));
			acquaintance.send(new SetInterfaceTextEvent(3431, ""));
		}
	}

	/**
	 * @param item
	 */
	public void removeOffer(Item item) {
		final TradeSession as = verifyAcquaintanceSession();
		if (as.state.ordinal() > 1 || state.ordinal() > 1)
			return;
		final int amountRemoved = offeredItems.remove(item);
		if (amountRemoved < 1)
			return;
		player.getInventory().add(item.getId(), amountRemoved);
		if (as.state == State.AWAITING_ACCEPTANCE || state == State.AWAITING_ACCEPTANCE) {
			state = State.TRADING;
			as.state = State.TRADING;
			player.send(new SetInterfaceTextEvent(3431, ""));
			acquaintance.send(new SetInterfaceTextEvent(3431, ""));
		}
	}

	State setState(State state) {
		return this.state = state;
	}

	private TradeSession verifyAcquaintanceSession() {
		final TradeSession as = acquaintance.getSettings().getTradeSession();
		if (as == null || as.acquaintance != player) {
			player.getSettings().setTradeSession(null);
			throw new RuntimeException("player mismatch in trade session.");
		}
		return as;
	}
}