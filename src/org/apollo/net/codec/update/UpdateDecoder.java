package org.apollo.net.codec.update;

import org.apollo.fs.FileDescriptor;
import org.apollo.net.codec.update.OnDemandRequest.Priority;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * A {@link FrameDecoder} for the 'on-demand' protocol.
 * @author Graham
 */
public final class UpdateDecoder extends FrameDecoder {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty
	 * .channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel c, ChannelBuffer buf) throws Exception {
		if (buf.readableBytes() >= 4) {
			final int type = buf.readUnsignedByte() + 1;
			final int file = buf.readUnsignedShort();
			final int priority = buf.readUnsignedByte();
			final FileDescriptor desc = new FileDescriptor(type, file);
			final Priority p = Priority.valueOf(priority);
			return new OnDemandRequest(desc, p);
		}
		return null;
	}
}
