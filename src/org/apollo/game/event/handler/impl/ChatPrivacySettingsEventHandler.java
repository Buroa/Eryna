package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ChatPrivacySettingsEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.PlayerSettings;

/**
 * An {@link EventHandler} for the {@link ChatPrivacySettingsEvent}
 * @author Steve
 */
public final class ChatPrivacySettingsEventHandler extends EventHandler<ChatPrivacySettingsEvent> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.game.event.handler.EventHandler#handle(org.apollo.game.event
	 * .handler.EventHandlerContext,
	 * org.apollo.game.model.Player, org.apollo.game.event.Event)
	 */
	@Override
	public void handle(EventHandlerContext ctx, Player player, ChatPrivacySettingsEvent event) {
		final PlayerSettings settings = player.getSettings();
		settings.setTrade(event.getTrade());
		settings.setPrivateChat(event.getPrivateChat());
		settings.setPublicChat(event.getPublicChat());
	}
}