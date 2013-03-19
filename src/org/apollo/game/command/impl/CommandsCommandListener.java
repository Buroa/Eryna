package org.apollo.game.command.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apollo.game.command.Command;
import org.apollo.game.command.CommandListener;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.quest.QuestConstants;

/**
 * An {@link CommandListener} which listens to {@code ::commands}
 * @author Steve
 */
public final class CommandsCommandListener implements CommandListener {

	/**
	 * The command listeners.
	 */
	private final Map<String, CommandListener> listeners;

	/**
	 * Creates the new command listener.
	 * @param listeners The command listeners.
	 */
	public CommandsCommandListener(Map<String, CommandListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void execute(Player player, Command command) {
		int pos = 0;
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "@dre@Commands"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], ""));
		for (final Entry<String, CommandListener> keys : listeners.entrySet())
			player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "::" + keys.getKey() + " - " + keys.getValue().getClass().getSimpleName()));
		for (; pos < QuestConstants.QUEST_TEXT.length; pos++)
			player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos], ""));
		player.getInterfaceSet().openWindow(QuestConstants.QUEST_INTERFACE);
	}

}
