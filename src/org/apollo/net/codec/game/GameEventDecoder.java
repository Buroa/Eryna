package org.apollo.net.codec.game;

import org.apollo.game.event.Event;
import org.apollo.net.release.EventDecoder;
import org.apollo.net.release.Release;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

/**
 * A {@link OneToOneDecoder} that decodes {@link GamePacket}s into {@link Event}
 * s.
 * @author Graham
 */
public final class GameEventDecoder extends OneToOneDecoder {

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * Creates the game event decoder with the specified release.
	 * @param release The release.
	 */
	public GameEventDecoder(Release release) {
		this.release = release;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneDecoder#decode(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel c, Object msg) throws Exception {
		if (msg instanceof GamePacket) {
			final GamePacket packet = (GamePacket) msg;
			final EventDecoder<?> decoder = release.getEventDecoder(packet.getOpcode());
			if (decoder != null)
				return decoder.decode(packet);
			return null;
		}
		return msg;
	}
}
