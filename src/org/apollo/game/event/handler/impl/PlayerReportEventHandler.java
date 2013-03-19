package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PlayerReportEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.util.NameUtil;

/**
 * An {@link EventHandler} for the {@link PlayerReportEvent}
 * @author Steve
 */
public final class PlayerReportEventHandler extends EventHandler<PlayerReportEvent> {

	/**
	 * The names of the reports.
	 */
	private final String[] REPORTS = { "Offensive Language", "Item Scamming", "Password Scamming",
			"Bug abuse", "3xgaming staff impersonation", "Account sharing/trading", "Macroing",
			"Multiple logging in", "Encouraging others to break rules", "Misuse of customer support",
			"Advertising / website", "Real world item trading" };

	@Override
	public void handle(EventHandlerContext ctx, Player player, PlayerReportEvent event) {
		final String report = REPORTS[event.getRule()];
		if (report == null) {
			ctx.breakHandlerChain();
			return;
		}
		final String victim = NameUtil.decodeBase37(event.getPlayer()).replace("_", " ");
		final Player victim_player = World.getWorld().getPlayer(victim);
		if (victim_player == null) {
			ctx.breakHandlerChain();
			return;
		}

		// Check if we can mute and if so, we need to be a moderator+
		if (event.isMutable())
			if (player.getPrivilegeLevel().toInteger() >= Player.PrivilegeLevel.MODERATOR.toInteger()) {
				victim_player.getSettings().setMute(true);
				victim_player.sendMessage("You have been muted for " + report + " by " + player.getName());
				player.sendMessage("You have muted " + victim_player.getName() + ".");
			}

		player.sendMessage("Thank you, your report has been processed.");
	}

}
