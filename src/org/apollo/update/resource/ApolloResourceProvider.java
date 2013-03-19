package org.apollo.update.resource;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apollo.game.command.Command;
import org.apollo.update.HttpRequestWorker;
import org.apollo.util.DefaultUtil;
import org.apollo.util.TextUtil;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * A {@link ResourceProvider} which provides web-enabled scripts to execute
 * commands.
 * @author Steve
 */
public final class ApolloResourceProvider extends ResourceProvider {

	/**
	 * The base directory from which documents are served.
	 */
	private final File base;

	/**
	 * Creates a new hypertext resource provider with the specified base
	 * directory.
	 * @param base The base directory.
	 */
	public ApolloResourceProvider(File base) {
		this.base = base;
	}

	@Override
	public boolean accept(String path) throws IOException {
		if (path.contains(".apollo"))
			return true;
		return false;
	}

	@Override
	public ByteBuffer get(Channel channel, HttpRequest request, String path) throws IOException {
		try {
			final StringBuilder builder = new StringBuilder();
			final File f = new File(base, path.split("\\?")[0]);
			if (!f.exists())
				return null;
			final List<String> lines = Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.defaultCharset());
			final Map<String, List<String>> parameters = TextUtil.getUrlParameters(path);
			for (String line : lines) {
				final String[] args = line.split(" ");
				for (int i = 0; i < args.length; i++) {
					final String argument = args[i];
					if (argument.startsWith("%")) {
						final String temp = argument.replaceFirst("%", "").replaceFirst("._if", "");
						final String replacement = parameters.get(temp).toString().replace("[", "").replace("]", "");
						if (argument.endsWith("._if")) {
							final String key = args[i + 1];
							if (key.equals(replacement)) {
								line = line.replaceFirst(key + " ", "");
								line = line.replaceFirst(argument + " ", "");
							}
							else
								return HttpRequestWorker.createErrorPage(HttpResponseStatus.UNAUTHORIZED,
										"You do not have permission to view this directory or page using the credentials that you supplied.").toByteBuffer();
						} else
							line = line.replaceFirst(argument, replacement);
					}
				}
				final Command command = DefaultUtil.createCommand(line);
				DefaultUtil.executeCommand(command);
				builder.append("Executed <font color='red'>" + command.toString() + "</font> on " + new Date() + "<br>");
			}
			return HttpRequestWorker.createErrorPage(HttpResponseStatus.CREATED, builder.toString()).toByteBuffer();
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
