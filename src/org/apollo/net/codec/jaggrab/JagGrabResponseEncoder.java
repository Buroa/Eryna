package org.apollo.net.codec.jaggrab;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * A {@link OneToOneEncoder} for the JAGGRAB protocol.
 * @author Graham
 */
public final class JagGrabResponseEncoder extends OneToOneEncoder {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel c, Object msg) throws Exception {
		if (msg.getClass() == JagGrabResponse.class) {
			final JagGrabResponse resp = (JagGrabResponse) msg;
			return resp.getFileData();
		}
		return msg;
	}
}
