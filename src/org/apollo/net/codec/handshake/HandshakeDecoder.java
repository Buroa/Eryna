package org.apollo.net.codec.handshake;

import org.apollo.game.model.World;
import org.apollo.net.codec.login.LoginDecoder;
import org.apollo.net.codec.login.LoginEncoder;
import org.apollo.net.codec.update.UpdateDecoder;
import org.apollo.net.codec.update.UpdateEncoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * A {@link FrameDecoder} which decodes the handshake and makes changes to the
 * pipeline as appropriate for the selected
 * service.
 * @author Graham
 */
public final class HandshakeDecoder extends FrameDecoder {

	/**
	 * Creates the handshake frame decoder.
	 */
	public HandshakeDecoder() {
		super(true);
	}

	/**
	 * Creates a channel buffer for the handshake.
	 * @param capacity The capacity of the buffer.
	 * @param handshake The handshake received from the client.
	 * @return The channel buffer.
	 */
	private ChannelBuffer createBuffer(int capacity, int handshake) {
		final ChannelBuffer buffer = ChannelBuffers.buffer(capacity);
		switch (handshake) {
		case HandshakeConstants.SERVICE_COUNT:
			buffer.writeShort(World.getWorld().getPlayerRepository().size());
			break;
		case HandshakeConstants.SERVICE_UPDATE:
			buffer.writeLong(0);
			break;
		}
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty
	 * .channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			final int id = buffer.readUnsignedByte();
			switch (id) {
			case HandshakeConstants.SERVICE_GAME:
				ctx.getPipeline().addFirst("loginEncoder", new LoginEncoder());
				ctx.getPipeline().addBefore("handler", "loginDecoder", new LoginDecoder());
				break;
			case HandshakeConstants.SERVICE_UPDATE:
				ctx.getPipeline().addFirst("updateEncoder", new UpdateEncoder());
				ctx.getPipeline().addBefore("handler", "updateDecoder", new UpdateDecoder());
				channel.write(createBuffer(8, id)); // TODO should it be here?
				break;
			case HandshakeConstants.SERVICE_COUNT:
				channel.write(createBuffer(2, id)); // TODO should it be here?
				break;
			case HandshakeConstants.SERVICE_WORLD:
				break;
			default:
				throw new Exception("Invalid service id " + id);
			}
			ctx.getPipeline().remove(this);
			final HandshakeMessage message = new HandshakeMessage(id);
			if (buffer.readable())
				return new Object[] { message, buffer.readBytes(buffer.readableBytes()) };
			else
				return message;
		}
		return null;
	}
}
