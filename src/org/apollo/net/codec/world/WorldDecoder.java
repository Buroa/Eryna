package org.apollo.net.codec.world;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import com.google.gson.Gson;

/**
 * A {@link OneToOneDecoder} for the world protocol.
 * @author Steve
 */
public final class WorldDecoder extends OneToOneDecoder {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneDecoder#decode(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel c, Object msg) throws Exception {
		if (msg instanceof String) {
			final String str = (String) msg;
			final Gson gson = new Gson();
			return gson.fromJson(str, WorldRequest.class);
		}
		return msg;
	}
}
