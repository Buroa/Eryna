package org.apollo.game.model.inter.trade;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Request;
import org.apollo.game.model.World;
import org.apollo.util.TextUtil;

/**
 * @author Steve
 */
public final class TradeUtilities {

	private static void initTrade(Player player, Player acquaintance) {
		player.getSettings().setRequest(null);
		final TradeSession s = player.getSettings().setTradeSession(new TradeSession(player, acquaintance));
		player.getInterfaceSet().openWindowWithSidebar(new TradeInterfaceListener(s),
				TradeConstants.TRADE_INTERFACE_ID, TradeConstants.INVENTORY_INTERFACE_ID);
		player.send(new SetInterfaceTextEvent(3417, "Trading with: " + TextUtil.capitalize(acquaintance.getName())));
		player.send(new SetInterfaceTextEvent(3431, ""));
		player.send(new UpdateItemsEvent(3322, player.getInventory().getItems()));
		player.send(new UpdateItemsEvent(3415, new Item[0]));
		player.send(new UpdateItemsEvent(3416, new Item[0]));
	}

	/**
	 * @param player
	 * @param acquaintance
	 */
	public static void sendTradeRequest(Player player, Player acquaintance) {
		if (!World.getWorld().isPlayerOnline(acquaintance.getName())
				|| !player.getPosition().isWithinDistance(acquaintance.getPosition(), 20)) {
			player.sendMessage("Could not find the player.");
			return;
		}
		final Request ar = acquaintance.getSettings().getRequest();
		if (ar != null && ar.getType() == Request.Type.TRADE_REQUEST && ar.getRecipient() == player)
			if (!ar.hasTimedOut(10000)) {
				initTrade(player, acquaintance);
				initTrade(acquaintance, player);
				return;
			}
			else
				acquaintance.getSettings().setRequest(null);
		final Request r = new Request(player, acquaintance, Request.Type.TRADE_REQUEST, Request.State.PENDING);
		player.getSettings().setRequest(r);
		player.sendMessage("Sending trade request...");
		acquaintance.sendMessage(TextUtil.capitalize(player.getName()) + ":tradereq:");
	}

	private TradeUtilities() {
		// ...
	}
}
