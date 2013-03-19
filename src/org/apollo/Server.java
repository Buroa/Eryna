package org.apollo;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.impl.ProcessGroundItemsTask;
import org.apollo.game.scheduling.impl.RandomNpcMovementScheduledTask;
import org.apollo.game.scheduling.impl.ShopsScheduledTask;
import org.apollo.io.player.DefaultPlayerListener;
import org.apollo.net.ApolloHandler;
import org.apollo.net.HttpPipelineFactory;
import org.apollo.net.JagGrabPipelineFactory;
import org.apollo.net.NetworkConstants;
import org.apollo.net.ServicePipelineFactory;
import org.apollo.net.WorldPipelineFactory;
import org.apollo.net.release.Release;
import org.apollo.net.release.r317.Release317;
import org.apollo.util.plugin.PluginContext;
import org.apollo.util.plugin.PluginManager;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

/**
 * The core class of the Apollo server.
 * @author Graham
 */
public final class Server {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	/**
	 * Gets the file seperator.
	 * @return The file seperator.
	 */
	public static String getSeperator() {
		return System.getProperty("file.separator");
	}

	/**
	 * Check if the filesystem is windows.
	 * @return True if windows, false if otherwise.
	 */
	public static boolean isWindows() {
		final String os = System.getProperty("os.name").toLowerCase();
		return os.indexOf("win") >= 0;
	}

	/**
	 * The entry point of the Apollo server application.
	 * @param args The command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		Server server = null;
		try {
			server = new Server();
			server.init(args.length == 1 ? args[0] : Release317.class.getName());
			final SocketAddress service = new InetSocketAddress(NetworkConstants.SERVICE_PORT);
			final SocketAddress http = new InetSocketAddress(NetworkConstants.HTTP_PORT);
			final SocketAddress jaggrab = new InetSocketAddress(NetworkConstants.JAGGRAB_PORT);
			final SocketAddress world = new InetSocketAddress(NetworkConstants.WORLD_PORT);
			server.start();
			server.bind(service, http, jaggrab, world);
		} catch (final Throwable t) {
			logger.log(Level.SEVERE, "Error whilst starting server.", t);
		}
	}

	/**
	 * The {@link ServerBootstrap} for the service listener.
	 */
	private final ServerBootstrap serviceBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the HTTP listener.
	 */
	private final ServerBootstrap httpBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the JAGGRAB listener.
	 */
	private final ServerBootstrap jagGrabBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the world listener.
	 */
	private final ServerBootstrap worldBootstrap = new ServerBootstrap();

	/**
	 * The {@link ExecutorService} used for network events. The named thread
	 * factory is unused as Netty names threads
	 * itself.
	 */
	private final ExecutorService networkExecutor = Executors.newCachedThreadPool();

	/**
	 * The service manager.
	 */
	private final ServiceManager serviceManager;

	/**
	 * The timer used for idle checking.
	 */
	private final Timer timer = new HashedWheelTimer();

	/**
	 * The server's context.
	 */
	private ServerContext context;

	/**
	 * Creates the Apollo server.
	 * @throws Exception if an error occurs whilst creating services.
	 */
	public Server() throws Exception {
		logger.info("Starting Apollo...");
		serviceManager = new ServiceManager();
		Runtime.getRuntime().addShutdownHook(new ServerHooks());
	}

	/**
	 * Binds the server to the specified address.
	 * @param serviceAddress The service address to bind to.
	 * @param httpAddress The HTTP address to bind to.
	 * @param jagGrabAddress The JAGGRAB address to bind to.
	 * @param worldAddress The world address to bind to.
	 */
	public void bind(SocketAddress serviceAddress, SocketAddress httpAddress, SocketAddress jagGrabAddress, SocketAddress worldAddress) {
		logger.info("Binding service listener to address: " + serviceAddress + "...");
		serviceBootstrap.bind(serviceAddress);
		logger.info("Binding HTTP listener to address: " + httpAddress + "...");
		try {
			httpBootstrap.bind(httpAddress);
		} catch (final Throwable t) {
			logger.log(Level.WARNING,
					"Binding to HTTP failed: client will use JAGGRAB as a fallback (not reccomended)!", t);
		}
		logger.info("Binding JAGGRAB listener to address: " + jagGrabAddress + "...");
		jagGrabBootstrap.bind(jagGrabAddress);
		logger.info("Binding world listener to address: " + worldAddress + "...");
		worldBootstrap.bind(worldAddress);
		logger.info("Ready for connections.");
	}

	/**
	 * Initialises the server.
	 * @param releaseClassName The class name of the current active
	 * {@link Release}.
	 * @throws ClassNotFoundException if the release class could not be found.
	 * @throws InstantiationException if the release class could not be
	 * instantiated.
	 * @throws IllegalAccessException if the release class could not be
	 * accessed.
	 */
	public void init(String releaseClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		final Class<?> clazz = Class.forName(releaseClassName);
		final Release release = (Release) clazz.newInstance();
		logger.info("Initialized release #" + release.getReleaseNumber() + ".");
		final ChannelFactory factory = new NioServerSocketChannelFactory(networkExecutor, networkExecutor);
		serviceBootstrap.setFactory(factory);
		httpBootstrap.setFactory(factory);
		jagGrabBootstrap.setFactory(factory);
		worldBootstrap.setFactory(factory);
		context = new ServerContext(release, serviceManager);
		final ApolloHandler handler = new ApolloHandler(context);
		final ChannelPipelineFactory servicePipelineFactory = new ServicePipelineFactory(handler, timer);
		serviceBootstrap.setPipelineFactory(servicePipelineFactory);
		final ChannelPipelineFactory httpPipelineFactory = new HttpPipelineFactory(handler, timer);
		httpBootstrap.setPipelineFactory(httpPipelineFactory);
		final ChannelPipelineFactory jagGrabPipelineFactory = new JagGrabPipelineFactory(handler, timer);
		jagGrabBootstrap.setPipelineFactory(jagGrabPipelineFactory);
		final ChannelPipelineFactory worldPipelineFactory = new WorldPipelineFactory(handler, timer);
		worldBootstrap.setPipelineFactory(worldPipelineFactory);
	}

	/**
	 * Starts the server.
	 * @throws Exception if an error occurs.
	 */
	public void start() throws Exception {
		final PluginManager mgr = new PluginManager(new PluginContext(context));
		serviceManager.startAll();
		final int releaseNo = context.getRelease().getReleaseNumber();
		final IndexedFileSystem fs = new IndexedFileSystem(new File("data/fs/" + releaseNo), true);
		World.getWorld().init(releaseNo, fs, mgr, context);
		mgr.start();
		startTasks(); // TODO Best way?
	}

	/**
	 * Starts the tasks required for this server.
	 */
	private void startTasks() {
		World.getWorld().schedule(new ProcessGroundItemsTask());
		World.getWorld().schedule(new ShopsScheduledTask());
		World.getWorld().schedule(new RandomNpcMovementScheduledTask());
		World.getWorld().register(new DefaultPlayerListener());
	}
}
