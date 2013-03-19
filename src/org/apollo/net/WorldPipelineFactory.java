package org.apollo.net;

import java.nio.charset.Charset;

import org.apollo.net.codec.world.WorldDecoder;
import org.apollo.net.codec.world.WorldEncoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.Timer;

/**
 * A {@link ChannelPipelineFactory} for the world protocol.
 * @author Steve
 */
public final class WorldPipelineFactory implements ChannelPipelineFactory {

	/**
	 * The character set used in the request.
	 */
	private static final Charset WORLD_CHARSET = Charset.forName("US-ASCII");

	/**
	 * The file server event handler.
	 */
	private final ApolloHandler handler;

	/**
	 * The timer used for idle checking.
	 */
	private final Timer timer;

	/**
	 * Creates a {@code JAGGRAB} pipeline factory.
	 * @param handler The file server event handler.
	 * @param timer The timer used for idle checking.
	 */
	public WorldPipelineFactory(ApolloHandler handler, Timer timer) {
		this.handler = handler;
		this.timer = timer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("string-decoder", new StringDecoder(WORLD_CHARSET));
		pipeline.addLast("world-decoder", new WorldDecoder());
		// encoders
		pipeline.addLast("string-encoder", new StringEncoder(WORLD_CHARSET));
		pipeline.addLast("world-encoder", new WorldEncoder());
		// handler
		pipeline.addLast("timeout", new IdleStateHandler(timer, NetworkConstants.IDLE_TIME, 0, 0));
		pipeline.addLast("handler", handler);
		return pipeline;
	}
}
