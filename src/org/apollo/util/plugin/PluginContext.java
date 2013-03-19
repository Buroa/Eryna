package org.apollo.util.plugin;

import org.apollo.ServerContext;
import org.apollo.game.GameService;
import org.apollo.game.command.CommandListener;
import org.apollo.game.event.Event;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.chain.EventHandlerChain;
import org.apollo.game.event.handler.chain.EventHandlerChainGroup;
import org.apollo.game.model.World;
import org.apollo.io.player.PlayerListener;
import org.apollo.net.release.EventDecoder;
import org.apollo.net.release.EventEncoder;
import org.apollo.net.release.Release;
import org.apollo.world.WorldListener;
import org.apollo.world.WorldModule;

/**
 * The {@link PluginContext} contains methods a plugin can use to interface with
 * the server, for example, by adding {@link EventHandler}s to
 * {@link EventHandlerChain}s.
 * @author Graham
 */
public final class PluginContext {

	/**
	 * The server context.
	 */
	private final ServerContext context;

	/**
	 * Creates the plugin context.
	 * @param context The server context.
	 */
	public PluginContext(ServerContext context) {
		this.context = context;
	}

	/**
	 * Adds a command listener.
	 * @param name The name of the listener.
	 * @param listener The listener.
	 */
	public void addCommandListener(String name, CommandListener listener) {
		World.getWorld().getCommandDispatcher().register(name, listener);
	}

	/**
	 * Adds an event decoder.
	 * @param <T> The type of decoder.
	 * @param releaseNo The release number.
	 * @param opcode The opcode.
	 * @param decoder The event decoder.
	 */
	public <T extends Event> void addEventDecoder(int releaseNo, int opcode, EventDecoder<T> decoder) {
		final Release release = context.getRelease();
		if (release.getReleaseNumber() != releaseNo)
			return;
		release.register(decoder, opcode);
	}

	/**
	 * Adds an event encoder.
	 * @param <T> The type of encoder.
	 * @param releaseNo The release number.
	 * @param event The event.
	 * @param encoder The event encoder.
	 */
	public <T extends Event> void addEventEncoder(int releaseNo, Class<T> event, EventEncoder<T> encoder) {
		final Release release = context.getRelease();
		if (release.getReleaseNumber() != releaseNo)
			return;
		release.register(event, encoder);
	}

	/**
	 * Adds an event handler to the end of the chain.
	 * @param <T> The type of event.
	 * @param event The event.
	 * @param handler The handler.
	 */
	public <T extends Event> void addLastEventHandler(Class<T> event, EventHandler<T> handler) {
		final EventHandlerChainGroup chains = context.getService(GameService.class).getEventHandlerChains();
		final EventHandlerChain<T> chain = chains.getChain(event);
		if (chain != null)
			chain.addLast(handler);
	}

	/**
	 * Adds a login listener.
	 * @param listener The listener.
	 */
	public void addPlayerListener(PlayerListener listener) {
		World.getWorld().register(listener);
	}

	/**
	 * Adds a world listener.
	 * @param module The module.
	 * @param command The command.
	 * @param listener The listener.
	 */
	public void addWorldListener(String module, String command, WorldListener listener) {
		World.getWorld().getWorldDispatcher().register(module, command, listener);
	}

	/**
	 * Adds a world module.
	 * @param module The world module.
	 */
	public void addWorldModule(WorldModule module) {
		World.getWorld().getWorldDispatcher().register(module);
	}
}
