package org.apollo.net.codec.jaggrab;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

/**
 * A {@link OneToOneDecoder} for the JAGGRAB protocol.
 * @author Graham
 */
public final class JagGrabRequestDecoder extends OneToOneDecoder {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneDecoder#decode(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel c, Object msg) throws Exception {
		if (msg.getClass() == String.class) {
			final String str = (String) msg;
			if (str.startsWith("JAGGRAB /")) {
				final String filePath = str.substring(8).trim();
				return new JagGrabRequest(filePath);
			}
			else
				throw new Exception("corrupted request line");
		}
		return msg;
	}
}
