package org.apollo.net.codec.login;

import java.security.SecureRandom;

import org.apollo.ServerSettings;
import org.apollo.game.model.World;
import org.apollo.util.StatefulFrameDecoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A {@link StatefulFrameDecoder} which decodes the login request frames.
 * @author Graham
 * @author Steve
 */
public abstract class LoginDecoder extends StatefulFrameDecoder<LoginDecoderState> {

	/**
	 * The server settings.
	 */
	protected static final ServerSettings serverSettings = World.getWorld().getServerSettings();

	/**
	 * The secure random number generator.
	 */
	protected static final SecureRandom random = new SecureRandom();

	/**
	 * The username hash.
	 */
	protected int usernameHash;

	/**
	 * The server-side session key.
	 */
	protected long serverSeed;

	/**
	 * The reconnecting flag.
	 */
	protected boolean reconnecting;

	/**
	 * The login packet length.
	 */
	protected int loginLength;

	/**
	 * The rsa login packet length.
	 */
	protected int loginEncryptPacketSize;

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
	abstract Object decodeHandshake(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception;

	/**
	 * Decodes in the header state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	abstract Object decodeHeader(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception;

	/**
	 * Decodes in the payload state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	abstract Object decodePayload(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception;
}
