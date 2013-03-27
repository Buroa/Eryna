package org.apollo.net.codec.login;

import java.math.BigInteger;
import java.security.SecureRandom;

import net.burtleburtle.bob.rand.IsaacRandom;

import org.apollo.ServerSettings;
import org.apollo.fs.FileSystemConstants;
import org.apollo.game.model.World;
import org.apollo.security.IsaacRandomPair;
import org.apollo.security.PlayerCredentials;
import org.apollo.util.ChannelBufferUtil;
import org.apollo.util.NameUtil;
import org.apollo.util.StatefulFrameDecoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A {@link StatefulFrameDecoder} which decodes the login request frames.
 * @author Graham
 */
public final class LoginDecoder extends StatefulFrameDecoder<LoginDecoderState> {
	
	/**
	 * The server settings.
	 */
	private static final ServerSettings serverSettings = World.getWorld().getServerSettings();

	/**
	 * The secure random number generator.
	 */
	private static final SecureRandom random = new SecureRandom();

	/**
	 * The username hash.
	 */
	private int usernameHash;

	/**
	 * The server-side session key.
	 */
	private long serverSeed;

	/**
	 * The reconnecting flag.
	 */
	private boolean reconnecting;

	/**
	 * The login packet length.
	 */
	private int loginLength;

	/**
	 * The rsa login packet length.
	 */
	private int loginEncryptPacketSize;

	/**
	 * Creates the login decoder with the default initial state.
	 */
	public LoginDecoder() {
		super(LoginDecoderState.LOGIN_HANDSHAKE, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.util.StatefulFrameDecoder#decode(org.jboss.netty.channel.
	 * ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer,
	 * java.lang.Enum)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, LoginDecoderState state)
			throws Exception {
		switch (state) {
		case LOGIN_HANDSHAKE:
			return decodeHandshake(ctx, channel, buffer);
		case LOGIN_HEADER:
			return decodeHeader(ctx, channel, buffer);
		case LOGIN_PAYLOAD:
			return decodePayload(ctx, channel, buffer);
		default:
			throw new Exception("Invalid login decoder state");
		}
	}

	/**
	 * Decodes in the handshake state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodeHandshake(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			usernameHash = buffer.readUnsignedByte();
			serverSeed = random.nextLong();
			final ChannelBuffer resp = ChannelBuffers.buffer(17);
			resp.writeByte(LoginConstants.STATUS_EXCHANGE_DATA);
			resp.writeLong(0);
			resp.writeLong(serverSeed);
			channel.write(resp);
			setState(LoginDecoderState.LOGIN_HEADER);
		}
		return null;
	}

	/**
	 * Decodes in the header state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodeHeader(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() >= 2) {
			final int loginType = buffer.readUnsignedByte();
			if (loginType != LoginConstants.TYPE_STANDARD && loginType != LoginConstants.TYPE_RECONNECTION)
				throw new Exception("Invalid login type");
			reconnecting = loginType == LoginConstants.TYPE_RECONNECTION;
			loginLength = buffer.readUnsignedByte();
			loginEncryptPacketSize = loginLength - (36 + 1 + 1 + 2);
			setState(LoginDecoderState.LOGIN_PAYLOAD);
		}
		return null;
	}

	/**
	 * Decodes in the payload state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodePayload(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() >= loginLength) {
			final ChannelBuffer payload = buffer.readBytes(loginLength);
			if (payload.readUnsignedByte() != 0xFF)
				throw new Exception("Invalid magic id");
			final int releaseNumber = payload.readUnsignedShort();
			final int lowMemoryFlag = payload.readUnsignedByte();
			if (lowMemoryFlag != 0 && lowMemoryFlag != 1)
				throw new Exception("Invalid value for low memory flag");
			final boolean lowMemory = lowMemoryFlag == 1;
			final int[] archiveCrcs = new int[FileSystemConstants.ARCHIVE_COUNT];
			for (int i = 0; i < 9; i++) {
				archiveCrcs[i] = payload.readInt();
			}
			if (serverSettings.isCrcEnabled()) {
				final int[] cacheCrcs = World.getWorld().getCrcs();
				for (int i = 0; i < 9; i++)
					if (archiveCrcs[i] != cacheCrcs[i])
						throw new Exception("CRC mismatch");
			}
			loginEncryptPacketSize--;
			final int securePayloadLength = payload.readUnsignedByte();
			if (loginEncryptPacketSize != securePayloadLength)
				throw new Exception("Secure payload length mismatch");
			ChannelBuffer securePayload = null;
			if (serverSettings.isRsaEnabled()) {
				final byte[] encryptionBytes = new byte[loginEncryptPacketSize];
				final BigInteger rsaExponent = serverSettings.getRsaExponentInteger();
				final BigInteger rsaModulus = serverSettings.getRsaModulusInteger();
				payload.readBytes(encryptionBytes);
				final BigInteger encryptionKey = new BigInteger(encryptionBytes).modPow(rsaExponent, rsaModulus);
				securePayload = ChannelBuffers.wrappedBuffer(encryptionKey.toByteArray());
			} else
				securePayload = payload.readBytes(loginEncryptPacketSize);
			final int secureId = securePayload.readUnsignedByte();
			if (secureId != 10)
				throw new Exception("Invalid secure payload id");
			final long clientSeed = securePayload.readLong();
			final long reportedServerSeed = securePayload.readLong();
			if (reportedServerSeed != serverSeed)
				throw new Exception("Server seed mismatch");
			final int uid = securePayload.readInt();
			final String username = ChannelBufferUtil.readString(securePayload);
			final String password = ChannelBufferUtil.readString(securePayload);
			if (username.length() > 12 || password.length() > 20)
				throw new Exception("Username or password too long");
			final long usernameLong = NameUtil.encodeBase37(username);
			final int finalUsernameHash = (int) (usernameLong >> 16 & 31L);
			if (usernameHash != finalUsernameHash)
				throw new Exception("Username hash mismatch");
			final int[] seed = new int[4];
			seed[0] = (int) (clientSeed >> 32);
			seed[1] = (int) clientSeed;
			seed[2] = (int) (serverSeed >> 32);
			seed[3] = (int) serverSeed;
			final IsaacRandom decodingRandom = new IsaacRandom(seed);
			for (int i = 0; i < seed.length; i++)
				seed[i] += 50;
			final IsaacRandom encodingRandom = new IsaacRandom(seed);
			final PlayerCredentials credentials = new PlayerCredentials(username, password, usernameHash, uid);
			final IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);
			final LoginRequest req = new LoginRequest(credentials, randomPair, reconnecting, lowMemory, releaseNumber,
					archiveCrcs);
			if (buffer.readable())
				return new Object[] { req, buffer.readBytes(buffer.readableBytes()) };
			else
				return req;
		}
		return null;
	}
}
