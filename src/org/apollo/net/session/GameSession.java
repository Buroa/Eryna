package org.apollo.net.session;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.ServerContext;
import org.apollo.ServerSettings;
import org.apollo.game.GameConstants;
import org.apollo.game.GameService;
import org.apollo.game.event.Event;
import org.apollo.game.event.handler.chain.EventHandlerChain;
import org.apollo.game.event.handler.chain.EventHandlerChainGroup;
import org.apollo.game.event.impl.DebugMessageEvent;
import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * A game session.
 * @author Graham
 */
public final class GameSession extends Session {
	
	/**
	 * The server settings.
	 */
	private static final ServerSettings serverSettings = World.getWorld().getServerSettings();

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(GameSession.class.getName());

	/**
	 * The server context.
	 */
	private final ServerContext context;

	/**
	 * The queue of pending {@link Event}s.
	 */
	private final BlockingQueue<Event> eventQueue = new ArrayBlockingQueue<Event>(GameConstants.EVENTS_PER_PULSE);

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates a login session for the specified channel.
	 * @param channel The channel.
	 * @param context The server context.
	 * @param player The player.
	 */
	public GameSession(Channel channel, ServerContext context, Player player) {
		super(channel);
		this.context = context;
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.net.session.Session#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		context.getService(GameService.class).unregisterPlayer(player);
	}

	/**
	 * Encodes and dispatches the specified event.
	 * @param event The event.
	 */
	public void dispatchEvent(Event event) {
		final Channel channel = getChannel();
		if (channel.isBound() && channel.isConnected() && channel.isOpen()) {
			final ChannelFuture future = channel.write(event);
			if (player.isHidden())
				if (event.getClass() != DebugMessageEvent.class)
					player.send(new DebugMessageEvent(event.getClass().getName(), 2));
			if (event.getClass() == LogoutEvent.class)
				future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	/**
	 * Handles the event.
	 * @param event The event to handle.
	 */
	@SuppressWarnings("unchecked")
	public void handleEvent(Event event) {
		final GameService gameService = context.getService(GameService.class);
		final EventHandlerChainGroup chainGroup = gameService.getEventHandlerChains();
		Class<? extends Event> eventType = event.getClass();
		EventHandlerChain<Event> chain = (EventHandlerChain<Event>) chainGroup.getChain(eventType);
		while (chain == null && eventType != null) {
			eventType = (Class<? extends Event>) eventType.getSuperclass();
			if (eventType == Event.class)
				eventType = null;
			else
				chain = (EventHandlerChain<Event>) chainGroup.getChain(eventType);
		}
		if (chain == null)
			logger.warning("No chain for event: " + event.getClass().getName() + ".");
		else
			try {
				chain.handle(player, event);
			} catch (final Exception ex) {
				logger.log(Level.SEVERE, "Error handling event.", ex);
			}
	}

	/**
	 * Handles pending events for this session.
	 * @param chainGroup The event chain group.
	 */
	@SuppressWarnings("unchecked")
	public void handlePendingEvents(EventHandlerChainGroup chainGroup) {
		for (final Event event : eventQueue) {
			Class<? extends Event> eventType = event.getClass();
			EventHandlerChain<Event> chain = (EventHandlerChain<Event>) chainGroup.getChain(eventType);
			while (chain == null && eventType != null) {
				eventType = (Class<? extends Event>) eventType.getSuperclass();
				if (eventType == Event.class)
					eventType = null;
				else
					chain = (EventHandlerChain<Event>) chainGroup.getChain(eventType);
			}
			if (chain == null)
				logger.warning("No chain for event: " + event.getClass().getName() + ".");
			else
				try {
					chain.handle(player, event);
				} catch (final Exception ex) {
					logger.log(Level.SEVERE, "Error handling event.", ex);
				}
		}
		eventQueue.clear();
	}

	/**
	 * Handles a player saver response.
	 * @param success A flag indicating if the save was successful.
	 */
	public void handlePlayerSaverResponse(boolean success) {
		context.getService(GameService.class).finalizePlayerUnregistration(player);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.net.session.Session#messageReceived(java.lang.Object)
	 */
	@Override
	public void messageReceived(Object message) throws Exception {
		final Event event = (Event) message;
		if (serverSettings.isPacketQueueEnabled() && !(eventQueue.size() >= GameConstants.EVENTS_PER_PULSE)) {
			if (player.getPrivilegeLevel().toInteger() >= 1) {
				handleEvent(event);
				if (player.isHidden())
					player.send(new DebugMessageEvent(event.getClass().toString(), 0));
			} else
				eventQueue.add(event);
		} else
			handleEvent(event);
	}
}
