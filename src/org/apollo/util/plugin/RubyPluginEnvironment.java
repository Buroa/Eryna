package org.apollo.util.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jruby.embed.ScriptingContainer;

/**
 * A {@link PluginEnvironment} which uses Ruby.
 * @author Graham
 */
public final class RubyPluginEnvironment implements PluginEnvironment {

	/**
	 * The scripting container.
	 */
	private final ScriptingContainer container = new ScriptingContainer();

	/**
	 * Creates and bootstraps the Ruby plugin environment.
	 * @throws IOException if an I/O error occurs during bootstrapping.
	 */
	public RubyPluginEnvironment() throws IOException {
		parseBootstrapper();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.util.plugin.PluginEnvironment#parse(java.io.InputStream,
	 * java.lang.String)
	 */
	@Override
	public void parse(InputStream is, String name) {
		container.runScriptlet(is, name);
	}

	/**
	 * Parses the bootstrapper.
	 * @throws IOException if an I/O error occurs.
	 */
	private void parseBootstrapper() throws IOException {
		final File f = new File("./data/plugins/bootstrap.rb");
		final InputStream is = new FileInputStream(f);
		try {
			parse(is, f.getAbsolutePath());
		} finally {
			is.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.util.plugin.PluginEnvironment#setContext(org.apollo.util.plugin
	 * .PluginContext)
	 */
	@Override
	public void setContext(PluginContext context) {
		container.put("$ctx", context);
	}
}
