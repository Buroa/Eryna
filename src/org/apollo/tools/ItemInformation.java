package org.apollo.tools;

import java.io.File;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.ItemDefinitionParser;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.util.RuneWiki;

/**
 * A tool for updating the equipment bonuses.
 * @author Graham
 * @author Palidino76
 * @author Steve
 */
public final class ItemInformation {

	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 * @throws Exception if an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage:");
			System.err.println("  java -cp ... org.apollo.tools.Test [release] [item]");
			return;
		}
		final String release = args[0];
		final int item = Integer.parseInt(args[1]);
		final IndexedFileSystem fs = new IndexedFileSystem(new File("data/fs/" + release), true);
		try {
			final ItemDefinitionParser parser = new ItemDefinitionParser(fs);
			final ItemDefinition[] defs = parser.parse();
			ItemDefinition.init(defs);
			final double[] bonuses = RuneWiki.getBonuses(item);
			int i = 0;
			for (final Double bonus : bonuses)
				System.out.println(EquipmentConstants.BONUS_NAMES[i++] + ": " + bonus);
			System.out.println("\n");
			final double weight = RuneWiki.getWeight(item);
			System.out.println("Weight: " + weight);
			System.out.println("\n");
			final int[] prices = RuneWiki.getAlchValues(item);
			i = 0;
			for (final Integer price : prices) {
				System.out.println((i == 0 ? "High" : "Low") + " Alch: " + price);
				i++;
			}
			System.out.println("\n");
			final int price = RuneWiki.getPrice(item);
			System.out.println("GE Price: " + price);
		} finally {
			fs.close();
		}
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private ItemInformation() {
	}
}
