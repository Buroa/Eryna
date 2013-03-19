package org.apollo.update.resource;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * A resource provider composed of multiple resource providers.
 * @author Graham
 */
public final class CombinedResourceProvider extends ResourceProvider {

	/**
	 * An array of resource providers.
	 */
	private final ResourceProvider[] providers;

	/**
	 * Creates the combined resource providers.
	 * @param providers The providers this provider delegates to.
	 */
	public CombinedResourceProvider(ResourceProvider... providers) {
		this.providers = providers;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.update.resource.ResourceProvider#accept(java.lang.String)
	 */
	@Override
	public boolean accept(String path) throws IOException {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.update.resource.ResourceProvider#get(java.lang.String)
	 */
	@Override
	public ByteBuffer get(Channel channel, HttpRequest request, String path) throws IOException {
		for (final ResourceProvider provider : providers)
			if (provider.accept(path))
				return provider.get(channel, request, path);
		return null;
	}
}
