package org.apollo.net.codec.login;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * A class which encodes login response messsages.
 * @author Graham
 */
public final class LoginEncoder extends OneToOneEncoder {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
		if (!(message.getClass() == LoginResponse.class))
			return message;
		final LoginResponse response = (LoginResponse) message;
		final ChannelBuffer buffer = ChannelBuffers.buffer(3);
		buffer.writeByte(response.getStatus());
		if (response.getStatus() == LoginConstants.STATUS_OK) {
			buffer.writeByte(response.getRights());
			buffer.writeByte(response.isFlagged() ? 1 : 0);
		}
		return buffer;
	}
}
