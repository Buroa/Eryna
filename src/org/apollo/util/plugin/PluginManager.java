package org.apollo.util.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apollo.io.PluginMetaDataParser;
import org.xml.sax.SAXException;

/**
 * A class which manages plugins.
 * @author Graham
 */
public final class PluginManager {

	/**
	 * The plugin context.
	 */
	private final PluginContext context;

	/**
	 * A set of all author names.
	 */
	private final SortedSet<String> authors = new TreeSet<String>();

	/**
	 * Creates the plugin manager.
	 * @param context The plugin context.
	 */
	public PluginManager(PluginContext context) {
		this.context = context;
		initAuthors();
	}

	/**
	 * Creates an iterator for the set of authors.
	 * @return The iterator.
	 */
	public Iterator<String> createAuthorsIterator() {
		return authors.iterator();
	}

	/**
	 * Creates a plugin map from a collection.
	 * @param plugins The plugin collection.
	 * @return The plugin map.
	 */
	private Map<String, PluginMetaData> createMap(Collection<PluginMetaData> plugins) {
		final Map<String, PluginMetaData> map = new HashMap<String, PluginMetaData>();
		for (final PluginMetaData plugin : plugins)
			map.put(plugin.getId(), plugin);
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Finds plugins and loads their meta data.
	 * @return A collection of plugin meta data objects.
	 * @throws IOException if an I/O error occurs.
	 * @throws SAXException if a SAX error occurs.
	 */
	private Collection<PluginMetaData> findPlugins() throws IOException, SAXException {
		final Collection<PluginMetaData> plugins = new ArrayList<PluginMetaData>();
		final File dir = new File("./data/plugins");
		for (final File plugin : dir.listFiles())
			if (plugin.isDirectory() && !plugin.getName().startsWith(".")) {
				final File xml = new File(plugin, "plugin.xml");
				if (xml.exists()) {
					final InputStream is = new FileInputStream(xml);
					try {
						final PluginMetaDataParser parser = new PluginMetaDataParser(is);
						final PluginMetaData meta = parser.parse();
						for (final String author : meta.getAuthors())
							authors.add(author);
						plugins.add(meta);
					} finally {
						is.close();
					}
				}
			}
		return Collections.unmodifiableCollection(plugins);
	}

	/**
	 * Populates the list with the authors of the Apollo core.
	 */
	private void initAuthors() {
		authors.add("Graham");
		authors.add("Blake");
	}

	/**
	 * Starts the plugin system by finding and loading all the plugins.
	 * @throws IOException if an I/O error occurs.
	 * @throws SAXException if a SAX error occurs.
	 * @throws DependencyException if a dependency could not be resolved.
	 */
	public void start() throws IOException, SAXException, DependencyException {
		final Map<String, PluginMetaData> plugins = createMap(findPlugins());
		final Set<PluginMetaData> started = new HashSet<PluginMetaData>();
		final PluginEnvironment env = new RubyPluginEnvironment();
		env.setContext(context);
		for (final PluginMetaData plugin : plugins.values())
			start(env, plugin, plugins, started);
	}

	/**
	 * Starts a specific plugin.
	 * @param env The environment.
	 * @param plugin The plugin.
	 * @param plugins The plugin map.
	 * @param started A set of started plugins.
	 * @throws DependencyException if a dependency error occurs.
	 * @throws IOException if an I/O error occurs.
	 */
	private void start(PluginEnvironment env, PluginMetaData plugin, Map<String, PluginMetaData> plugins,
			Set<PluginMetaData> started) throws DependencyException, IOException {
		if (started.contains(plugin))
			return;
		started.add(plugin);
		for (final String dependencyId : plugin.getDependencies()) {
			final PluginMetaData dependency = plugins.get(dependencyId);
			if (dependency == null)
				throw new DependencyException("Unresolved dependency: " + dependencyId + ".");
			start(env, dependency, plugins, started);
		}
		final String[] scripts = plugin.getScripts();
		for (final String script : scripts) {
			final File f = new File("./data/plugins/" + plugin.getId() + "/" + script);
			final InputStream is = new FileInputStream(f);
			env.parse(is, f.getAbsolutePath());
		}
	}
}
