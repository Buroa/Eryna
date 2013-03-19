package org.apollo.net.session;

import org.apollo.ServerContext;
import org.apollo.game.model.World;
import org.apollo.net.codec.world.WorldRequest;
import org.apollo.net.codec.world.WorldResponse;
import org.jboss.netty.channel.Channel;

/**
 * A world session.
 * @author Steve
 */
public final class WorldSession extends Session {

	/**
	 * The server context.
	 */
	@SuppressWarnings("unused")
	private final ServerContext serverContext;

	/**
	 * Creates a world session for the specified channel.
	 * @param channel The channel.
	 * @param serverContext The server context.
	 */
	public WorldSession(Channel channel, ServerContext serverContext) {
		super(channel);
		this.serverContext = serverContext;
	}

	@Override
	public void destroy() throws Exception {
	}

	/**
	 * Handles a response from the world service.
	 * @param message The world request.
	 */
	private void handleWorldRequest(WorldRequest message) {
		World.getWorld().getWorldDispatcher().dispatch(this, message);
	}

	@Override
	public void messageReceived(Object message) throws Exception {
		if (message instanceof WorldRequest)
			handleWorldRequest((WorldRequest) message);
	}

	/**
	 * Encodes and dispatches the specified response.
	 * @param response The world response.
	 */
	public void send(WorldResponse response) {
		final Channel channel = getChannel();
		if (channel.isBound() && channel.isConnected() && channel.isOpen()) {
			channel.write(response);
			channel.close();
		}
	}

}
