package org.apollo.update;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.game.model.World;
import org.apollo.update.resource.ApolloResourceProvider;
import org.apollo.update.resource.CombinedResourceProvider;
import org.apollo.update.resource.HypertextResourceProvider;
import org.apollo.update.resource.ResourceProvider;
import org.apollo.update.resource.VirtualResourceProvider;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * A worker which services HTTP requests.
 * @author Graham
 */
public final class HttpRequestWorker extends RequestWorker<HttpRequest, ResourceProvider> {

	/**
	 * The directory with web files.
	 */
	private static final File WWW_DIRECTORY = new File("./data/www/");

	/**
	 * The default character set.
	 */
	public static final Charset CHARACTER_SET = Charset.forName("ISO-8859-1");

	/**
	 * Creates an error page.
	 * @param status The HTTP status.
	 * @param description The error description.
	 * @return The error page as a buffer.
	 */
	public static ChannelBuffer createErrorPage(HttpResponseStatus status, String description) {
		final String title = status.getCode() + " " + status.getReasonPhrase();
		final StringBuilder bldr = new StringBuilder();
		bldr.append("<!DOCTYPE html><html><head><title>");
		bldr.append(title);
		bldr.append("</title></head><body><h1>");
		bldr.append(title);
		bldr.append("</h1><p>");
		bldr.append(description);
		bldr.append("</p><hr /><address>");
		bldr.append(World.getWorld().getServerSettings().getServerName());
		bldr.append(" Server</address></body></html>");
		return ChannelBuffers.copiedBuffer(bldr.toString(), Charset.defaultCharset());
	}

	/**
	 * Creates the HTTP request worker.
	 * @param dispatcher The dispatcher.
	 * @param fs The file system.
	 */
	public HttpRequestWorker(UpdateDispatcher dispatcher, IndexedFileSystem fs) {
		super(dispatcher, new CombinedResourceProvider(new VirtualResourceProvider(fs), new ApolloResourceProvider(
				WWW_DIRECTORY), new HypertextResourceProvider(WWW_DIRECTORY)));
	}

	/**
	 * Gets the MIME type of a file by its name.
	 * @param name The file name.
	 * @return The MIME type.
	 */
	private String getMimeType(String name) {
		if (name.endsWith(".htm") || name.endsWith(".html") || name.endsWith("/"))
			return "text/html";
		else if (name.endsWith(".css"))
			return "text/css";
		else if (name.endsWith(".js"))
			return "text/javascript";
		else if (name.endsWith(".jpg") || name.endsWith(".jpeg"))
			return "image/jpeg";
		else if (name.endsWith(".gif"))
			return "image/gif";
		else if (name.endsWith(".png"))
			return "image/png";
		else if (name.endsWith(".txt"))
			return "text/plain";
		else if (name.contains(".apollo"))
			return "text/html";
		return "application/octect-stream";
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.update.RequestWorker#nextRequest(org.apollo.update.
	 * UpdateDispatcher)
	 */
	@Override
	protected ChannelRequest<HttpRequest> nextRequest(UpdateDispatcher dispatcher) throws InterruptedException {
		return dispatcher.nextHttpRequest();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.update.RequestWorker#service(java.lang.Object,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected void service(ResourceProvider provider, Channel channel, HttpRequest request) throws IOException {
		final String path = request.getUri();
		final ByteBuffer buf = provider.get(channel, request, path);
		HttpResponseStatus status = HttpResponseStatus.OK;
		String mimeType = getMimeType(request.getUri());
		ChannelBuffer wrappedBuf = null;

		if (buf == null) {
			status = HttpResponseStatus.NOT_FOUND;
			wrappedBuf = createErrorPage(status, "The page you requested could not be found.");
			mimeType = "text/html";
		} else
			wrappedBuf = ChannelBuffers.wrappedBuffer(buf);

		final HttpResponse resp = new DefaultHttpResponse(request.getProtocolVersion(), status);
		resp.setHeader("Date", new Date());
		resp.setHeader("Server", World.getWorld().getServerSettings().getServerName());
		resp.setHeader("Content-type", mimeType + ", charset=" + CHARACTER_SET.name());
		resp.setHeader("Cache-control", "no-cache");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Expires", new Date(0));
		resp.setHeader("Connection", "close");
		resp.setHeader("Content-length", wrappedBuf.readableBytes());
		resp.setChunked(false);
		resp.setContent(wrappedBuf);

		channel.write(resp).addListener(ChannelFutureListener.CLOSE);
	}
}
