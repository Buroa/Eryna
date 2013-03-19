package org.apollo.net.codec.update;

import org.apollo.fs.FileDescriptor;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * A {@link OneToOneEncoder} for the 'on-demand' protocol.
 * @author Graham
 */
public final class UpdateEncoder extends OneToOneEncoder {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel c, Object msg) throws Exception {
		if (msg.getClass() == OnDemandResponse.class) {
			final OnDemandResponse resp = (OnDemandResponse) msg;
			final FileDescriptor fileDescriptor = resp.getFileDescriptor();
			final int fileSize = resp.getFileSize();
			final int chunkId = resp.getChunkId();
			final ChannelBuffer chunkData = resp.getChunkData();
			final ChannelBuffer buf = ChannelBuffers.buffer(6 + chunkData.readableBytes());
			buf.writeByte(fileDescriptor.getType() - 1);
			buf.writeShort(fileDescriptor.getFile());
			buf.writeShort(fileSize);
			buf.writeByte(chunkId);
			buf.writeBytes(chunkData);
			return buf;
		}
		return msg;
	}
}
