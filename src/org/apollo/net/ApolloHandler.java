package org.apollo.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.ServerContext;
import org.apollo.ServerSettings;
import org.apollo.game.model.World;
import org.apollo.net.codec.handshake.HandshakeConstants;
import org.apollo.net.codec.handshake.HandshakeMessage;
import org.apollo.net.codec.jaggrab.JagGrabRequest;
import org.apollo.net.codec.world.WorldRequest;
import org.apollo.net.session.LoginSession;
import org.apollo.net.session.Session;
import org.apollo.net.session.UpdateSession;
import org.apollo.net.session.WorldSession;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
 * An implementation of {@link SimpleChannelUpstreamHandler} which handles
 * incoming upstream events from Netty.
 * @author Graham
 */
public final class ApolloHandler extends IdleStateAwareChannelUpstreamHandler {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ApolloHandler.class.getName());

	/**
	 * The server context.
	 */
	private final ServerContext serverContext;

	/**
	 * Creates the Apollo event handler.
	 * @param context The server context.
	 */
	public ApolloHandler(ServerContext context) {
		this.serverContext = context;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelConnected
	 * (org.jboss.netty.channel.ChannelHandlerContext ,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		final Channel channel = ctx.getChannel();
		final ServerSettings serverSettings = World.getWorld().getServerSettings();
		if (serverSettings.isShowingConnections())
			logger.info("Channel connected: " + channel);
		serverContext.getChannelGroup().add(channel);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelDisconnected
	 * (org.jboss.netty.channel.
	 * ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		final Channel channel = ctx.getChannel();
		final ServerSettings serverSettings = World.getWorld().getServerSettings();
		if (serverSettings.isShowingConnections())
			logger.info("Channel disconnected: " + channel);
		serverContext.getChannelGroup().remove(channel);
		final Object attachment = ctx.getAttachment();
		if (attachment != null)
			((Session) attachment).destroy();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler#
	 * channelIdle(org.jboss.netty.channel.
	 * ChannelHandlerContext, org.jboss.netty.handler.timeout.IdleStateEvent)
	 */
	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		e.getChannel().close();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#exceptionCaught(
	 * org.jboss.netty.channel.ChannelHandlerContext ,
	 * org.jboss.netty.channel.ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		logger.log(Level.WARNING, "Exception occured for channel: " + e.getChannel() + ", closing...", e.getCause());
		ctx.getChannel().close();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(
	 * org.jboss.netty.channel.ChannelHandlerContext ,
	 * org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (ctx.getAttachment() == null) {
			final Object msg = e.getMessage();
			if (msg instanceof HttpRequest || msg instanceof JagGrabRequest) {
				final Session s = new UpdateSession(ctx.getChannel(), serverContext);
				s.messageReceived(msg);
			} else if (msg instanceof WorldRequest) {
				final Session s = new WorldSession(ctx.getChannel(), serverContext);
				ctx.setAttachment(s);
				s.messageReceived(msg);
			} else {
				final HandshakeMessage handshakeMessage = (HandshakeMessage) msg;
				switch (handshakeMessage.getServiceId()) {
				case HandshakeConstants.SERVICE_GAME:
					ctx.setAttachment(new LoginSession(ctx.getChannel(), ctx, serverContext));
					break;
				case HandshakeConstants.SERVICE_UPDATE:
					ctx.setAttachment(new UpdateSession(ctx.getChannel(), serverContext));
					break;
				case HandshakeConstants.SERVICE_WORLD:
					ctx.setAttachment(new WorldSession(ctx.getChannel(), serverContext));
					break;
				case HandshakeConstants.SERVICE_COUNT:
					ctx.getChannel().close();
					break;
				default:
					throw new Exception("Invalid service id");
				}
			}
		} else
			((Session) ctx.getAttachment()).messageReceived(e.getMessage());
	}
}
