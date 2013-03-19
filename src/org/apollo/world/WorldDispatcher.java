package org.apollo.world;

import java.util.HashMap;
import java.util.Map;

import org.apollo.net.codec.world.WorldRequest;
import org.apollo.net.codec.world.WorldResponse;
import org.apollo.net.session.WorldSession;

/**
 * A class which dispatches {@link WorldRequest}s to {@link WorldListener}s.
 * @author Steve
 */
public final class WorldDispatcher {

	/**
	 * A list of avaliable modules.
	 */
	private final Map<String, WorldModule> modules = new HashMap<String, WorldModule>();

	/**
	 * A list of avaliable listeners.
	 */
	private final Map<WorldModule, Map<String, WorldListener>> listeners = new HashMap<WorldModule, Map<String, WorldListener>>();

	/**
	 * Registers some modules.
	 */
	public WorldDispatcher() {
		register(new WorldModule("account"));
		register(new WorldModule("server", true));
	}

	/**
	 * Dispatches a world request.
	 * @param session The world session.
	 * @param request The world request.
	 */
	public void dispatch(WorldSession session, WorldRequest request) {
		final WorldModule module = modules.get(request.getModule());
		if (module != null) {
			final WorldListener listener = listeners.get(module).get(request.getCommand());
			if (listener != null) {
				if (module.isAuthenticationRequired())
					if (request.getAuthentication() != null)
						;
					else {
						session.send(WorldResponse.AUTHENTICATION_REQUIRED);
						return;
					}
				session.send(listener.execute(request));
			} else
				session.send(WorldResponse.NO_SUCH_COMMAND);
		} else
			session.send(WorldResponse.NO_SUCH_MODULE);
	}

	/**
	 * Registeres a module listener.
	 * @param module The module.
	 * @param command The command.
	 * @param listener The listener.
	 */
	public void register(String module, String command, WorldListener listener) {
		final WorldModule _module = modules.get(module);
		if (_module != null)
			listeners.get(_module).put(command, listener);
	}

	/**
	 * Registes a world module.
	 * @param module The module to register.
	 */
	public void register(WorldModule module) {
		if (!listeners.containsKey(module)) {
			listeners.put(module, new HashMap<String, WorldListener>());
			modules.put(module.getModule(), module);
		}
	}

}
