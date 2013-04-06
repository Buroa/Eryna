package org.apollo.net.codec.login;

import net.burtleburtle.bob.rand.IsaacRandom;

import org.apollo.security.IsaacRandomPair;
import org.apollo.security.PlayerCredentials;
import org.apollo.util.ChannelBufferUtil;
import org.apollo.util.NameUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A {@link LoginDecoder} which decodes the login request frames for a 530
 * protocol.
 * @author Steve
 */
public final class LoginDecoder530 extends LoginDecoder {

	@Override
	Object decodeHandshake(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			usernameHash = buffer.readUnsignedByte();
			serverSeed = random.nextLong();
			final ChannelBuffer resp = ChannelBuffers.buffer(9);
			resp.writeByte(0); // OK
			resp.writeLong(serverSeed);
			channel.write(resp);
			setState(LoginDecoderState.LOGIN_HEADER);
		}
		return null;
	}

	@Override
	Object decodeHeader(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() >= 3) {
			final int loginType = buffer.readUnsignedByte();

			if (loginType != 16 && loginType != 18)
				throw new Exception("Invalid login type");

			reconnecting = loginType == 18;

			loginLength = buffer.readUnsignedShort();
			setState(LoginDecoderState.LOGIN_PAYLOAD);
		}
		return null;
	}

	@Override
	Object decodePayload(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() >= loginLength) {
			final ChannelBuffer payload = buffer.readBytes(loginLength);
			final int releaseNumber = payload.readInt();

			payload.readUnsignedByte();

			final int lowMemoryFlag = payload.readUnsignedByte();
			if (lowMemoryFlag != 0 && lowMemoryFlag != 1)
				throw new Exception("Invalid value for low memory flag");

			final boolean lowMemory = lowMemoryFlag == 1;

			final int magicId = payload.readUnsignedByte();
			if (magicId != 1)
				throw new Exception("Invalid magic id");

			payload.readUnsignedByte();
			payload.readUnsignedShort();
			payload.readUnsignedShort();
			payload.readUnsignedByte();

			for (int i = 0; i < 24; i++)
				payload.readUnsignedByte();

			final String settings = ChannelBufferUtil.readString(payload);

			payload.readInt();
			payload.readInt();

			payload.readUnsignedShort();

			final int[] archiveCrcs = new int[28];
			for (int i = 0; i < 28; i++)
				archiveCrcs[i] = payload.readInt();

			loginLength--;

			final int securePayloadLength = payload.readUnsignedByte();
			if (securePayloadLength != loginLength - 159 - (settings.length() + 1))
				throw new Exception("Secure payload length mismatch: [" + securePayloadLength + ":" + (loginLength - 159 - (settings.length() + 1)));

			final ChannelBuffer securePayload = payload.readBytes(securePayloadLength);

			final int secureId = securePayload.readUnsignedByte();
			if (secureId != 10)
				throw new Exception("Invalid secure payload id");

			final long clientSeed = securePayload.readLong();
			final long reportedServerSeed = securePayload.readLong();
			if (reportedServerSeed != serverSeed)
				throw new Exception("Server seed mismatch");

			final String username = NameUtil.decodeBase37(securePayload.readLong());
			final String password = ChannelBufferUtil.readString(securePayload);

			if (username.length() > 12 || password.length() > 20)
				throw new Exception("Username or password too long");

			final int[] seed = new int[4];
			seed[0] = (int) (clientSeed >> 32);
			seed[1] = (int) clientSeed;
			seed[2] = (int) (serverSeed >> 32);
			seed[3] = (int) serverSeed;

			final IsaacRandom decodingRandom = new IsaacRandom(seed);
			for (int i = 0; i < seed.length; i++)
				seed[i] += 50;
			final IsaacRandom encodingRandom = new IsaacRandom(seed);

			final PlayerCredentials credentials = new PlayerCredentials(username, password, usernameHash, 0);
			final IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);
			final LoginRequest req = new LoginRequest(credentials, randomPair, reconnecting, lowMemory, releaseNumber, archiveCrcs);
			if (buffer.readable())
				return new Object[] { req, buffer.readBytes(buffer.readableBytes()) };
			else
				return req;
		}
		return null;
	}

}
