package org.apollo.net.codec.game;

import net.burtleburtle.bob.rand.IsaacRandom;

import org.apollo.net.meta.PacketType;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * A {@link OneToOneEncoder} which encodes in-game packets.
 * @author Graham
 */
public final class GamePacketEncoder extends OneToOneEncoder {

	/**
	 * The random number generator.
	 */
	private final IsaacRandom random;

	/**
	 * Creates the {@link GamePacketEncoder}.
	 * @param random The random number generator.
	 */
	public GamePacketEncoder(IsaacRandom random) {
		this.random = random;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if (!(msg instanceof GamePacket))
			return msg;
		final GamePacket packet = (GamePacket) msg;
		final PacketType type = packet.getType();
		int headerLength = 1;
		final int payloadLength = packet.getLength();
		if (type == PacketType.VARIABLE_BYTE) {
			headerLength++;
			if (payloadLength >= 256)
				throw new Exception("Payload too long for variable byte packet");
		}
		else if (type == PacketType.VARIABLE_SHORT) {
			headerLength += 2;
			if (payloadLength >= 65536)
				throw new Exception("Payload too long for variable short packet");
		}
		final ChannelBuffer buffer = ChannelBuffers.buffer(headerLength + payloadLength);
		buffer.writeByte(packet.getOpcode() + random.nextInt() & 0xFF);
		if (type == PacketType.VARIABLE_BYTE)
			buffer.writeByte(payloadLength);
		else if (type == PacketType.VARIABLE_SHORT)
			buffer.writeShort(payloadLength);
		buffer.writeBytes(packet.getPayload());
		return buffer;
	}
}
