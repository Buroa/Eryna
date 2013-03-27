package org.apollo.io.player;

import org.apollo.game.event.impl.BuildPlayerMenuEvent;
import org.apollo.game.event.impl.ChatPrivacySettingsEvent;
import org.apollo.game.event.impl.IdAssignmentEvent;
import org.apollo.game.event.impl.SwitchTabInterfaceEvent;
import org.apollo.game.event.impl.UpdateRunEnergyEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.PlayerConstants;
import org.apollo.game.model.PlayerSettings;
import org.apollo.game.model.World;

/**
 * An {@link PlayerListener} which listens for login and logouts.
 * @author Steve
 */
public final class DefaultPlayerListener extends PlayerListener {

	@Override
	public void login(Player player) {
		final PlayerSettings settings = player.getSettings();
		player.send(new IdAssignmentEvent(player.getIndex(), settings.isMembers()));
		player.sendMessage("Wecome to " + World.getWorld().getServerSettings().getServerName() + ".");
		// character design screen
		if (!settings.hasDesignedCharacter())
			player.getInterfaceSet().openWindow(3559);
		// send tabs
		for (int i = 0; i < PlayerConstants.TABS.length; i++)
			player.send(new SwitchTabInterfaceEvent(i, PlayerConstants.TABS[i]));
		// force inventories to update
		player.getInventory().forceRefresh();
		player.getEquipment().forceRefresh();
		player.getBank().forceRefresh();
		// force skills to update
		player.getSkillSet().forceRefresh();
		// send context menus
		player.send(new BuildPlayerMenuEvent(4, false, "Follow"));
		player.send(new BuildPlayerMenuEvent(5, false, "Trade with"));
		// send privacy settings
		player.send(new ChatPrivacySettingsEvent(settings.getPublicChat(), settings.getPrivateChat(), settings.getTrade()));
		// send the run
		player.send(new UpdateRunEnergyEvent(player.getRunEnergy()));
	}

	@Override
	public void logout(Player player) {
		player.getInterfaceSet().interfaceClosed();
	}

}
