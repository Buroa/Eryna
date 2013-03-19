package org.apollo.tools;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.ItemDefinitionParser;
import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.io.player.PlayerLoader;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.io.player.PlayerSaver;
import org.apollo.io.player.impl.BinaryPlayerSaver;
import org.apollo.io.player.impl.DummyPlayerLoader;
import org.apollo.security.PlayerCredentials;

/**
 * A tool for updating the note data.
 * @author Graham
 */
public final class ItemSearch {

	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 * @throws Exception if an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.err.println("Usage:");
			System.err.println("  java -cp ... org.apollo.tools.ItemSearch [release] [item] [amount] [delete=false]");
			return;
		}
		int count = 0;
		final List<Player> players = new LinkedList<Player>();
		final int release = Integer.parseInt(args[0]);
		final int item = Integer.parseInt(args[1]);
		final int amount = Integer.parseInt(args[2]);
		final boolean delete = args.length > 3 ? Boolean.parseBoolean(args[3]) : false;
		final PlayerLoader loader = new DummyPlayerLoader();
		final PlayerSaver saver = new BinaryPlayerSaver();
		final File[] files = new File("data/savedGames/").listFiles();
		final IndexedFileSystem fs = new IndexedFileSystem(new File("data/fs/" + release), true);
		try {
			final ItemDefinitionParser parser = new ItemDefinitionParser(fs);
			final ItemDefinition[] defs = parser.parse();
			ItemDefinition.init(defs);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			for (final File file : files) {
				final String name = file.getName().replace(".dat", "");
				try {
					boolean flagged = false;
					final PlayerLoaderResponse response = loader.loadPlayer(new PlayerCredentials(name, "", 0, 0));
					final Player player = response.getPlayer();
					if (player.getPrivilegeLevel().equals(PrivilegeLevel.DEVELOPER))
						break;
					if (player.getBank().contains(item, amount)) {
						if (delete)
							player.getBank().remove(item);
						flagged = true;
					}
					if (player.getInventory().contains(item, amount)) {
						if (delete)
							player.getInventory().remove(item);
						flagged = true;
					}
					if (player.getEquipment().contains(item, amount)) {
						if (delete)
							player.getEquipment().remove(item);
						flagged = true;
					}
					if (flagged) {
						players.add(player);
						count++;
						if (delete)
							saver.savePlayer(player);
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("Out of " + files.length + " characters we found " + count);
		if (count > 0)
			System.out.println(players.toString());
	}
}
