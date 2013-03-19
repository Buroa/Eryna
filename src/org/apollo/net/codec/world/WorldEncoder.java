package org.apollo.net.codec.world;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.google.gson.Gson;

/**
 * A {@link OneToOneEncoder} for the world protocol.
 * @author Steve
 */
public final class WorldEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel c, Object msg) throws Exception {
		if (msg.getClass() == WorldResponse.class) {
			final WorldResponse resp = (WorldResponse) msg;
			final Gson gson = new Gson();
			return gson.toJson(resp);
		}
		return msg;
	}
}
